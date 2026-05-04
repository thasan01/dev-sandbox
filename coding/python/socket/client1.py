# This is a sample client that opens an image and sends the bytes to a server

import io
from PIL import Image
import numpy as np
import socket
import pickle
import time

HOST = "127.0.0.1"  # The server's hostname or IP address
PORT = 65432  # The port used by the server

with (
    socket.socket(socket.AF_INET, socket.SOCK_STREAM) as client,
    open("data/zelda_000.png", mode='rb') as file
):
    img_data = file.read()

    client.connect((HOST, PORT))
    client.sendall(img_data)
