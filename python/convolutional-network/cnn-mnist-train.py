import torch
import torch.optim as optim
import torch.nn as nn
from torch.utils.data import DataLoader
import torchvision.transforms as tr
import torchvision.datasets as datasets
import matplotlib.pyplot as plt
import importlib


cnn_module = importlib.import_module("cnn-mnist-model")
DigitCNN = cnn_module.DigitCNN
Stats = cnn_module.Stats
model_filepath = cnn_module.default_model_path
stats_filepath = cnn_module.default_train_stats_path

mean = 0.1207  # the average of the entire dataset
std_dev = 0.3081  # the standard deviation of the entire dataset
img_width = 28  # the width of the original image
img_height = 28  # the height of the original image


def show_image(dataset, index):
    img = dataset[index][0].numpy() * std_dev + mean
    plt.cla()
    plt.imshow(img.reshape(img_width, img_height), cmap="gray")
    plt.show()


# Define the transformation logic to normalize each channel of the input image:
# normal_image[channel] = (input[channel] - mean[channel]) / std_dev[channel]
transforms = tr.Compose([tr.ToTensor(), tr.Normalize((mean,), (std_dev,))])

# Download the MNIST train & test data to the "data" folder, and apply the transformation logic
# and finally assign the normalized output to the variables.
# If they are already downloaded, then load them from the file instead.
train_dataset = datasets.MNIST(root="./data", train=True, transform=transforms, download=True)
test_dataset = datasets.MNIST(root="./data", train=False, transform=transforms, download=True)
print(f"train_dataset={len(train_dataset)}, test_dataset={len(test_dataset)}")
# Debug: show_image(train_dataset, 0)

batch_size = 100
train_loader = DataLoader(dataset=train_dataset, batch_size=batch_size, shuffle=True)
test_loader = DataLoader(dataset=test_dataset, batch_size=batch_size, shuffle=False)
print(f"batch_size={batch_size}, train_loader={len(train_loader)}, test_loader={len(test_loader)}")

model = DigitCNN()
cuda = torch.cuda.is_available()
if cuda:
    model = model.cuda()

loss_fn = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=0.01)

# Train the CNN
num_epochs = 20
stats = Stats(num_epochs=num_epochs, dataset_size=len(train_dataset), batch_size=batch_size, mean=mean, std_dev=std_dev, img_width=img_width, img_height=img_height)

# this is required because the model is using batch normalization & dropout
model.train()

for epoch in range(num_epochs):
    iterations = 0
    correct = 0
    iter_loss = 0.0

    for i, (inputs, labels) in enumerate(train_loader):

        if cuda:
            inputs = inputs.cuda()
            labels = labels.cuda()

        # All Tensors propagated through the network should be a 4D tensor: [batch_size, #channels, #rows, #columns]
        # Labels shape = [100] because there are 100 values in a batch
        # Output shape = [100, 10] because there are 100 images in a batch and each image has 10 classes
        outputs = model(inputs)
        loss = loss_fn(outputs, labels)
        iter_loss += loss.item()

        optimizer.zero_grad()  # clear weights from previous step
        loss.backward()  # back propagation
        optimizer.step()  # update weights

        # calculate the accuracy
        _, predicted = torch.max(outputs, 1)
        # (predicted == labels) will create a tensor 1s where they match,and 0 otherwise
        correct += (predicted == labels).sum().item()
        iterations += 1

    stats.add(accuracy=correct / len(train_dataset), loss=iter_loss / iterations)

torch.save(model, model_filepath)
print(f"train_loss: {stats.losses}")
print(f"train_accuracy: {stats.accuracies}")
cnn_module.save_stats(stats_filepath, stats)
