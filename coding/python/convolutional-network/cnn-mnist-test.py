import torch
import torch.optim as optim
import torch.nn as nn
from torch.utils.data import DataLoader
import torchvision.transforms as tr
import torchvision.datasets as datasets
import importlib

mean = 0.1207  # the average of the entire dataset
std_dev = 0.3081  # the standard deviation of the entire dataset
img_width = 28  # the width of the original image
img_height = 28  # the height of the original image

cnn_module = importlib.import_module("cnn-mnist-model")
DigitCNN = cnn_module.DigitCNN
default_model_path = cnn_module.default_model_path
cuda = torch.cuda.is_available()

model = torch.load(default_model_path)
if cuda:
    model = model.cuda()

transforms = tr.Compose([tr.ToTensor(), tr.Normalize((mean,), (std_dev,))])
test_dataset = datasets.MNIST(root="./data", train=False, transform=transforms, download=True)
print(f"test_dataset={len(test_dataset)}")

batch_size = 100
test_loader = DataLoader(dataset=test_dataset, batch_size=batch_size, shuffle=False)

loss_fn = nn.CrossEntropyLoss()
optimizer = optim.Adam(model.parameters(), lr=0.01)

test_loss = []
test_accuracy = []
iterations = 0
iter_loss = 0.0
correct = 0

model.eval()

for i, (inputs, labels) in enumerate(test_loader):

    if cuda:
        inputs = inputs.cuda()
        labels = labels.cuda()

    # All Tensors propagated through the network should be a 4D tensor: [batch_size, #channels, #rows, #columns]
    # Labels shape = [100] because there are 100 values in a batch
    # Output shape = [100, 10] because there are 100 images in a batch and each image has 10 classes
    outputs = model(inputs)
    loss = loss_fn(outputs, labels)
    iter_loss += loss.item()

    # calculate the accuracy
    _, predicted = torch.max(outputs, 1)

    # (predicted == labels) will create a tensor 1s where they match,and 0 otherwise
    correct += (predicted == labels).sum().item()
    iterations += 1

test_loss.append(iter_loss / iterations)
test_accuracy.append(correct / len(test_dataset))

print(f"train_loss: {test_loss}")
print(f"train_accuracy: {test_accuracy}")
