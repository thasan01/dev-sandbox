# This is a sample socket that recieve a image ( in bytes), then create a 
# Pillow Image object and display it in a window

import time
import socket
import numpy as np
import io
from PIL import Image

HOST = "127.0.0.1"  # Standard loopback interface address (localhost)
PORT = 65432  # Port to listen on (non-privileged ports are > 1023)

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
        
        img = Image.open(io.BytesIO(bytes(buffer)))
        img.show()
