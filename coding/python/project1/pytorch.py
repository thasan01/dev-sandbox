import torch
import numpy as np

print('')
print('Tensor Initialization')
print('=====================')

# create a 1D tensor
a = torch.tensor([2, 2, 1])
print('a = ' + str(a))
print('a datatype = ' + str(a.dtype))

fa = torch.FloatTensor([2, 2, 1])
print('fa = ' + str(fa))
print('a datatype = ' + str(fa.dtype))

# create a 2D tensor
b = torch.tensor([[1, 2], [4, 5], [7, 8]])
print('b = ' + str(b))
print('b shape = ' + str(b.shape))
print('b resize(-1,3) = ' + str(b.view(-1, 3)))

# Create a 4x3 tensor where each value is selected from normal distribution with mean = 0 variance = 1
c = torch.rand(4, 3)
print('c = ' + str(c))


print('')
print('Tensor operations')
print('=====================')
# Add 2 tensors
a1 = torch.rand(4, 4)
a2 = torch.rand(4, 4)
a3 = torch.add(a1, a2)  # both tensors must be same dimensions and data types
print('a1 ='+str(a1))
print('a2 ='+str(a2))
print('a3 (a1+a2) ='+str(a2))

a2.add_(a1)  # inplace addition, the result is stored into 'a2'
print('updated a2 ='+str(a2))


print('')
print('Tensor Slicing')
print('=====================')
print('a1 ='+str(a1))
print('a1[:0] = '+str(a1[:1]))  # get all rows from the first column

print('')
print('Numpy Bridge')
print('=====================')
# Tensor to numpy
a = torch.ones(5)
print('tensor: '+str(a)+' converted to numpy: '+str(a.numpy()))
a.add_(1.5)
# Updating the tensor also updates the numpy array
print('After addition - tensor: '+str(a)+' numpy: '+str(a.numpy()))

# Numpy to tensor
na = np.ones(5)
ta = torch.from_numpy(na)
print('numpy: '+str(na)+' converted to tensor: '+str(ta))
np.add(na, 1.5, out=na)
# Updating the numpy array also updates the tensor
print('After addition - numpy: '+str(na)+' tensor: '+str(ta))


print('')
print('GPU Tensor')
print('=====================')
ta = torch.ones(5)
gpu_support = torch.cuda.is_available()
print('GPU supported: '+str(gpu_support))

if gpu_support:
    ta = ta.cuda()

print('ta '+str(ta))
