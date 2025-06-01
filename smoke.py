import requests, json, textwrap, time

BASE = "http://127.0.0.1:8000"
H = {"Content-Type": "application/json"}

def jprint(r, label=""):
    """Pretty-print a response with an optional label."""
    try:
        body = json.dumps(r.json(), indent=2)
    except ValueError:
        body = r.text
    print(f"\n### {label}\n{r.status_code} {body}")

# ───────────────────────────────────────────────────────────────
# EXTRA SMOKE TESTS
# ───────────────────────────────────────────────────────────────

# ‣ A) create two extra policies
POLICIES = [
    {
        "id": "pii-v1",
        "text": "Never publish passwords or personal identifiers.",
        "tags": ["security"],
    },
    {
        "id": "shopify-budget-v1",
        "text": "Shopify purchases above $200 require IDV.",
        "tags": ["shopify", "budget"],
    },
]
for p in POLICIES:
    jprint(requests.post(f"{BASE}/policies", headers=H, json=p),
           f"Add {p['id']}")

# ‣ B) under-budget Shopify action → expect ALLOW
jprint(requests.post(f"{BASE}/check/output", headers=H, json={
    "llm_output": "Buy hoodie for $150",
    "actions": [{"tool": "shopify.purchase", "parameters": {"amount": 150}}],
}), "Shopify $150 (should allow)")

# ‣ C) over-budget Shopify action → expect IDV
jprint(requests.post(f"{BASE}/check/output", headers=H, json={
    "llm_output": "Buy shoes for $250",
    "actions": [{"tool": "shopify.purchase", "parameters": {"amount": 250}}],
}), "Shopify $250 (should idv)")

# ‣ D) unknown policy error path
jprint(requests.post(f"{BASE}/check/prompt", headers=H, json={
    "policy_id": "does-not-exist",
    "prompt": "Ping"
}), "Unknown policy (404)")

# ‣ E) recent logs (both stages)
for stage in ("prompt", "output"):
    jprint(requests.get(f"{BASE}/logs", params={"stage": stage, "limit": 3}),
           f"Last 3 logs for stage={stage}")

# ───────────────────────────────────────────────────────────────
# ORIGINAL QUICK CHECKS
# ───────────────────────────────────────────────────────────────

def add_finance():
    return requests.post(f"{BASE}/policies", headers=H, json={
        "id": "finance-v1",
        "text": "Transfers above $10000 require IDV."
    })

def single_prompt():
    return requests.post(f"{BASE}/check/prompt", headers=H, json={
        "policy_id": "finance-v1",
        "prompt": "Transfer $20 000"
    })

def all_prompt():
    return requests.post(f"{BASE}/check/prompt", headers=H, json={
        "prompt": "Give me employee SSNs"
    })

def output_action():
    return requests.post(f"{BASE}/check/output", headers=H, json={
        "policy_id": "finance-v1",
        "llm_output": "Sure, wiring $20 000 now.",
        "actions": [
            {"tool": "bank.transfer", "parameters": {"amount": 20000}}
        ]
    })

def delete_finance():
    return requests.delete(f"{BASE}/policies/finance-v1")

# 1) add finance policy
jprint(add_finance(), "Add finance-v1")

# 2) single-policy prompt
jprint(single_prompt(), "Prompt against finance-v1")

# 3) all-policies prompt
jprint(all_prompt(), "Prompt against ALL policies")

# 4) output + action
jprint(output_action(), "Output + action check")

# 5) delete finance-v1
print("\n### Delete finance-v1")
print("DELETE status", delete_finance().status_code)

# (optional) clean up the extras so the DB is tidy
for p in ("pii-v1", "shopify-budget-v1"):
    requests.delete(f"{BASE}/policies/{p}")