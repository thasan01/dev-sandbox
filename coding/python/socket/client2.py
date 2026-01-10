# This is a sample client that opens an image and converts the bytes to a 
# numpy array. The array sent to a server.

import io
from PIL import Image
import numpy as np
import socket
import pickle
import time

HOST = "127.0.0.1"
PORT = 65432  

with (
    socket.socket(socket.AF_INET, socket.SOCK_STREAM) as client,
    Image.open("data/zelda_000.png") as im
):
    img_arr = np.array(im)

    client.connect((HOST, PORT))
    client.sendall(img_arr.tobytes())
