
import os
from azure.identity import DefaultAzureCredential
from azure.ai.projects import AIProjectClient

# 1. Define your project endpoint and agent details
PROJECT_ENDPOINT = "https://ronin-test1-resource.services.ai.azure.com/api/projects/ronin-test1" # os.getenv("FOUNDRY_PROJECT_ENDPOINT", "https://<your-resource>.ai.azure.com/api/projects/<your-project>")
AGENT_NAME = "expense-agent" #"your_deployed_agent_name"

# 1. Initialize the AI Project Client
project_client = AIProjectClient(
    endpoint=PROJECT_ENDPOINT,
    credential=DefaultAzureCredential()
)
print("Successfully connected to Azure AI Project.")

# 2. Get the OpenAI client for managing conversations and responses
openai_client = project_client.get_openai_client()

# 3. Create a server-side conversation (This replaces project_client.agents.create_thread)
conversation = openai_client.conversations.create()
print(f"Created conversation, ID: {conversation.id}")

# 4. Interact with your deployed agent (This replaces create_message + create_run + loop polling)
response = openai_client.responses.create(
    conversation=conversation.id,
    extra_body={
        "agent_reference": {
            "name": AGENT_NAME,  # Your agent's name
            "type": "agent_reference"
        }
    },
    input="What are the details of my latest expense report?"
)

# 5. Print the agent's response directly
print(f"Agent response: {response.output_text}")