const API_BASE = "";

function setToken(data) {
    localStorage.setItem("token", data.token);
    localStorage.setItem("user", JSON.stringify(data.user));
}

function getToken() {
    return localStorage.getItem("token");
}

function getUser() {
    const user = localStorage.getItem("user");
    return user ? JSON.parse(user) : null;
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    window.location.href = 'index.html';
}

function checkAuth() {
    if (!getToken()) {
        window.location.href = 'index.html';
    }
}

async function apiCall(endpoint, method = 'GET', body = null) {
    const headers = { 'Content-Type': 'application/json' };
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;

    const opts = { method, headers };
    if (body) opts.body = JSON.stringify(body);

    try {
        const res = await fetch(`${API_BASE}${endpoint}`, opts);
        
        let json;
        try { json = await res.json(); } catch(e) { json = {}; }
        
        if (res.status === 401) {
            throw new Error(json.message || "Invalid email or password");
        }
        if (!res.ok) {
            // Check for structured validation errors or fallback
            if (json.errors && json.errors.length > 0) {
                throw new Error(json.errors[0]);
            }
            throw new Error(json.error || json.message || "Something went wrong. Try again");
        }
        return json.data || json;
    } catch (err) {
        if (err.message === "Failed to fetch") {
            throw new Error("Something went wrong. Try again");
        }
        throw err;
    }
}

function formatCurrency(amt) {
    return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(amt);
}

function formatDate(dateStr) {
    return new Date(dateStr).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
}
