# Streamlit dashboard for Policy API
# Run: streamlit run policy_dashboard.py
# -------------------------------------------------------------
# * Cleaner look & feel
# * Logs (prompts / actions) tucked under collapsible sections
# -------------------------------------------------------------

import streamlit as st
import requests, json
from datetime import datetime as dt

API = "http://localhost:8000"  # adjust if backend runs elsewhere

# ╭──────────────────────── helper functions ───────────────────╮

def fetch_json(endpoint: str):
    try:
        resp = requests.get(f"{API}{endpoint}")
        resp.raise_for_status()
        return resp.json()
    except Exception as e:
        st.error(f"Backend error on {endpoint}: {e}")
        return []

def add_policy(pid: str, text: str, tags):
    r = requests.post(
        f"{API}/policies",
        json={"id": pid, "text": text, "tags": tags},
    )
    return r.ok, r.text

def delete_policy(pid: str):
    r = requests.delete(f"{API}/policies/{pid}")
    return r.ok, r.text

# ╭──────────────────────── page layout ────────────────────────╮

st.set_page_config(page_title="Policy Dashboard", layout="wide")
st.title("🛡️ Policy Dashboard")

# ╭──────────────────────── Add / Update form ───────────────────╮
with st.expander("➕ Add / Update Policy", expanded=True):
    with st.form("add_policy_form"):
        cid = st.text_input("Policy ID (unique)")
        ctext = st.text_area("Policy text", height=150)
        ctags = st.text_input("Tags (comma-separated)")
        col_a, col_b = st.columns([1, 5])
        with col_a:
            submitted = st.form_submit_button("💾 Save")
        if submitted:
            tags_list = [t.strip() for t in ctags.split(",") if t.strip()]
            ok, msg = add_policy(cid.strip(), ctext, tags_list)
            if ok:
                st.success("Policy stored ✔️")
                st.rerun()
            else:
                st.error(f"Backend error: {msg}")

st.divider()

# ╭──────────────────────── Existing policies ───────────────────╮

st.subheader("📜 Existing Policies")
policies = fetch_json("/policies")

if not policies:
    st.info("No policies found.")
else:
    for p in policies:
        with st.expander(f"{p['id']}  (v{p['version']})"):
            st.markdown(f"**Tags:** {', '.join(p['tags']) or '—'}")
            st.code(p["text"], language="markdown")
            cols = st.columns([1, 6])
            with cols[0]:
                if st.button("🗑 Delete", key=f"del_{p['id']}"):
                    ok, msg = delete_policy(p["id"])
                    if ok:
                        st.success("Deleted ✔️")
                        st.rerun()
                    else:
                        st.error(f"Backend error: {msg}")
            with cols[1]:
                st.write("")  # spacer

st.divider()

# ╭──────────────────────── Logs (collapsed) ────────────────────╮

with st.expander("📝 Recent Prompts", expanded=False):
    prompts = fetch_json("/logs?stage=prompt&limit=20")
    if prompts:
        for item in prompts:
            ts = dt.fromisoformat(item["timestamp"]).strftime("%Y-%m-%d %H:%M:%S")
            st.markdown(f"- `{ts}` · **{item['policy_id']}** · {item['content']['prompt'][:150]}…")
    else:
        st.info("No prompt logs.")

with st.expander("⚙️ Recent Actions", expanded=False):
    actions = fetch_json("/logs?stage=output&limit=20")
    if actions:
        for item in actions:
            ts = dt.fromisoformat(item["timestamp"]).strftime("%Y-%m-%d %H:%M:%S")
            act_list = item["content"].get("actions", [])
            if act_list:
                first = act_list[0]
                descr = f"{first.get('tool','?')} {first.get('parameters',{})}"
            else:
                descr = "—"
            st.markdown(f"- `{ts}` · **{item['policy_id']}** · {descr[:150]}…")
    else:
        st.info("No action logs.")