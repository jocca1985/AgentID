import requests
import sseclient

url = "http://localhost:8000/sse"
client = sseclient.SSEClient(url)
for event in client:
    print(event.data)