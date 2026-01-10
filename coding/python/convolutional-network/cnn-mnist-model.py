import torch
import torch.nn as nn
import pickle
from typing import TextIO

default_model_path = "./data/model/digit-model.pt"
default_train_stats_path = "./data/model/train-stats.dat"
default_test_stats_path = "./data/model/test-stats.dat"

"""
Neural Network Architecture
    Input: MNIST grayscale image
    Conv layer1: 8 feature maps from 3x3 filters
    MaxPool
    Conv Layer 2: 32 feature maps
    MaxPool
    Fully Connected (FC) layer: 600
    Output (FC) layer: 10
"""


class DigitCNN(nn.Module):
    def __init__(self):
        super(DigitCNN, self).__init__()
        # in_channels = 1 for grayscale
        # out_channels = # of filters
        # kernel_size = 3x3 filter

        # same padding = input size will be same as output size
        #   (filter_size - 1) / 2
        #       -> (3 - 1) / 2 = 1
        # output sizeof each feature map:

        # (input_size - filter_size + 2 * padding) / stride + 1
        #   -> (28 - 3 + 2*1) / 1 + 1 = 28

        self.cnn1 = nn.Conv2d(in_channels=1, out_channels=8, kernel_size=3, stride=1, padding=1)
        self.batch_norm1 = nn.BatchNorm2d(8)
        self.relu = nn.ReLU()  # shared
        self.maxpool = nn.MaxPool2d(kernel_size=2)  # output size: 28 / 2 = 14

        # Second convolution layer
        # Same padding: (5 - 1)/ 2 = 2
        self.cnn2 = nn.Conv2d(in_channels=8, out_channels=32, kernel_size=5, stride=1, padding=2)
        # output size of each filter map is 14 (same padding)
        self.batch_norm2 = nn.BatchNorm2d(32)  # output size : 14 / 2 = 7
        # Flatten the 32 feature maps: 7*7*32 = 1568
        self.fc1 = nn.Linear(1568, 600)
        self.dropout = nn.Dropout(p=0.5)
        self.fc2 = nn.Linear(600, 10)

    def forward(self, x):
        # conv layer 1
        out = self.cnn1(x)
        out = self.batch_norm1(out)
        out = self.relu(out)
        out = self.maxpool(out)
        # conv layer 2
        out = self.cnn2(out)
        out = self.batch_norm2(out)
        out = self.relu(out)
        out = self.maxpool(out)
        # flatten the 32 feature maps to feed it to the fc layer: (100, 1568)
        out = out.view(-1, 1568)  # -1 tells python tp figure out the x size
        # fc layer
        out = self.fc1(out)
        out = self.relu(out)
        out = self.dropout(out)
        # output layer
        out = self.fc2(out)
        return out


class Stats:
    def __init__(self, num_epochs, dataset_size, batch_size, mean, std_dev, img_width, img_height):
        self.accuracies = []
        self.losses = []
        self.num_epochs = num_epochs
        self.dataset_size = dataset_size
        self.batch_size = batch_size
        self.mean = mean
        self.std_dev = std_dev
        self.img_width = img_width
        self.img_height = img_height

    def add(self, accuracy, loss):
        self.accuracies.append(accuracy)
        self.losses.append(loss)


def save_stats(filename, stats):
    file: TextIO
    with open(filename, "wb") as file:
        pickle.dump(stats, file)


def load_stats(filename):
    with open(filename, "rb") as file:
        return pickle.load(file)
