

CREATE TABLE query_sessions (
    session_id TEXT PRIMARY KEY,
    prompt TEXT,
    policy_decision TEXT,
    policy_triggered TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP
);

CREATE TABLE actions (
    action_id TEXT PRIMARY KEY,
    session_id TEXT REFERENCES query_sessions(session_id) ON DELETE CASCADE,
    plan TEXT,
    plan_status TEXT,
    loop_count INTEGER,
    start_time TIMESTAMP,
    end_time TIMESTAMP
);

CREATE TABLE policy_checks (
    policy_check_id TEXT PRIMARY KEY,
    action_id TEXT REFERENCES actions(action_id) ON DELETE CASCADE,
    decision TEXT,
    policy_triggered TEXT,
    policy_status TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP
);

CREATE TABLE tool_requests (
    tool_request_id TEXT PRIMARY KEY,
    action_id TEXT REFERENCES actions(action_id) ON DELETE CASCADE,
    policy_id TEXT REFERENCES policy_checks(policy_check_id) ON DELETE CASCADE,
    tool_status TEXT, -- Can be policy_denied, policy_pending, initiated, pending, completed, failed, timeout
    exec_start_time TIMESTAMP,
    exec_end_time TIMESTAMP,
    tool_name TEXT
);

CREATE TABLE feedback (
    action_id TEXT REFERENCES actions(action_id) ON DELETE CASCADE,
    feedback TEXT,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    decision TEXT -- Just continue_loop or end_loop, could've made a bool but might be more decision options later
);