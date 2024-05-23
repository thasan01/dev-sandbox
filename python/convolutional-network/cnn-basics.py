import torch
import torch.nn as nn

conv1 = nn.Conv2d(in_channels=1, out_channels=8, kernel_size=3, stride=1, padding=1)

x = torch.Tensor([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
x1, x2, x3 = x

print(x1)
#print(conv1(x))
