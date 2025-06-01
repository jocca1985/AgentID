// DOM Elements
const balanceElement = document.getElementById('balance');
const depositAmountInput = document.getElementById('deposit-amount');
const depositBtn = document.getElementById('deposit-btn');
const depositMessage = document.getElementById('deposit-message');
const withdrawAmountInput = document.getElementById('withdraw-amount');
const withdrawBtn = document.getElementById('withdraw-btn');
const withdrawMessage = document.getElementById('withdraw-message');

// API endpoints
const API_URL = 'http://localhost:5025/api';
const BALANCE_ENDPOINT = `${API_URL}/balance`;
const DEPOSIT_ENDPOINT = `${API_URL}/deposit`;
const WITHDRAW_ENDPOINT = `${API_URL}/withdraw`;

// Format currency
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
    }).format(amount);
}

// Show message
function showMessage(element, message, isError = false) {
    element.textContent = message;
    element.className = 'message ' + (isError ? 'error' : 'success');

    // Clear message after 5 seconds
    setTimeout(() => {
        element.textContent = '';
        element.className = 'message';
    }, 5000);
}

// Get account balance
async function getBalance() {
    try {
        const response = await fetch(BALANCE_ENDPOINT);
        const data = await response.json();

        if (response.ok) {
            balanceElement.textContent = formatCurrency(data.balance);
        } else {
            console.error('Error fetching balance:', data.error);
        }
    } catch (error) {
        console.error('Error fetching balance:', error);
    }
}

// Make a deposit
async function makeDeposit() {
    const amount = parseFloat(depositAmountInput.value);

    if (isNaN(amount) || amount <= 0) {
        showMessage(depositMessage, 'Please enter a valid amount', true);
        return;
    }

    try {
        // Get token from local storage (would be set by MCP)
        const token = localStorage.getItem('userToken');

        const headers = {
            'Content-Type': 'application/json'
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(DEPOSIT_ENDPOINT, {
            method: 'POST',
            headers,
            body: JSON.stringify({ amount })
        });

        const data = await response.json();

        if (response.ok) {
            balanceElement.textContent = formatCurrency(data.newBalance);
            depositAmountInput.value = '';
            showMessage(depositMessage, `Successfully deposited ${formatCurrency(amount)}`);
        } else {
            showMessage(depositMessage, data.error || 'Error making deposit', true);
        }
    } catch (error) {
        showMessage(depositMessage, 'Error making deposit', true);
        console.error('Error making deposit:', error);
    }
}

// Make a withdrawal
async function makeWithdrawal() {
    const amount = parseFloat(withdrawAmountInput.value);

    if (isNaN(amount) || amount <= 0) {
        showMessage(withdrawMessage, 'Please enter a valid amount', true);
        return;
    }

    try {
        // Get token from local storage (would be set by MCP)
        const token = localStorage.getItem('userToken');

        const headers = {
            'Content-Type': 'application/json'
        };

        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }

        const response = await fetch(WITHDRAW_ENDPOINT, {
            method: 'POST',
            headers,
            body: JSON.stringify({ amount })
        });

        const data = await response.json();

        if (response.ok) {
            balanceElement.textContent = formatCurrency(data.newBalance);
            withdrawAmountInput.value = '';
            showMessage(withdrawMessage, `Successfully withdrew ${formatCurrency(amount)}`);
        } else {
            showMessage(withdrawMessage, data.error || 'Error making withdrawal', true);
        }
    } catch (error) {
        showMessage(withdrawMessage, 'Error making withdrawal', true);
        console.error('Error making withdrawal:', error);
    }
}

// Event listeners
depositBtn.addEventListener('click', makeDeposit);
withdrawBtn.addEventListener('click', makeWithdrawal);

// Set token from URL if present (for testing)
function checkUrlForToken() {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (token) {
        localStorage.setItem('userToken', token);
        // Remove token from URL
        window.history.replaceState({}, document.title, window.location.pathname);
    }
}

// Initialize app
function init() {
    getBalance();
    checkUrlForToken();
}

// Start the app
init();