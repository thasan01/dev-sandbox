# This is a sample socket that recieve a image ( in bytes), then create a 
# Pillow Image object and display it in a window

import time
import socket
import numpy as np
import io
from PIL import Image

HOST = "127.0.0.1"
PORT = 65432

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()
    while True:
        conn, addr = s.accept()
        with conn:
            print(f"Connected by {addr}")
            buffer = bytearray()

            while True:
                data = conn.recv(1024)

                if not data:
                    break

                buffer += bytearray(data)

        img_arr = np.frombuffer(bytes(buffer), dtype=np.uint8)
        img_arr = img_arr.reshape(240, 256, 3)
        print(f"img_arr: {img_arr.shape}")
        img = Image.fromarray(img_arr.astype('uint8'), 'RGB')
        img.show()

