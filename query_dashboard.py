import streamlit as st
import requests
import json
from datetime import datetime as dt

# Configuration
ENGINE_URL = "http://localhost:5920"

# Helper functions
def send_prompt(prompt: str):
    try:
        response = requests.post(
            f"{ENGINE_URL}/test-controller-agent",
            json={"prompt": prompt}
        )
        response.raise_for_status()
        return response.json()
    except Exception as e:
        st.error(f"Error sending prompt: {e}")
        return None

# Page layout
st.set_page_config(page_title="AI Agent Dashboard", layout="wide")
st.title("AI Agent Dashboard")

# Prompt input
with st.form("prompt_form"):
    prompt = st.text_area(
        "Enter your prompt",
        height=150,
        placeholder="Example: check my bank balance"
    )
    submitted = st.form_submit_button("Send Prompt")

# Handle form submission
if submitted and prompt:
    with st.spinner("Processing..."):
        result = send_prompt(prompt)
        print(result)

# Add a divider
st.divider()

# Add a section for recent prompts (if you want to store history)
with st.expander("📝 Recent Prompts", expanded=False):
    st.info("Prompt history will be shown here")
    # You could add a database or file to store history