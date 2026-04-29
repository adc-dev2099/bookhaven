/* ═══════════════════════════════════════════════════
   BOOKHAVEN — SHARED JS
   api.js — config, auth helpers, api layer, utilities
═══════════════════════════════════════════════════ */

const API_BASE = window.location.origin;

// ─── TOKEN STORAGE ────────────────────────────────
const Auth = {
  getToken()    { return localStorage.getItem('bh_token'); },
  getUser()     { return localStorage.getItem('bh_user'); },
  getRole()     { return localStorage.getItem('bh_role'); },
  setSession(token, username, role) {
    localStorage.setItem('bh_token', token);
    localStorage.setItem('bh_user', username);
    localStorage.setItem('bh_role', (role || 'USER').toUpperCase());
  },
  clear() {
    localStorage.removeItem('bh_token');
    localStorage.removeItem('bh_user');
    localStorage.removeItem('bh_role');
  },
  isLoggedIn()  { return !!this.getToken(); },
  isAdmin() { return (this.getRole() || '').toUpperCase() === 'ADMIN'; },
  headers(json = true) {
    const h = {};
    if (json) h['Content-Type'] = 'application/json';
    if (this.getToken()) h['Authorization'] = `Bearer ${this.getToken()}`;
    return h;
  }
};

// ─── HTTP HELPERS ─────────────────────────────────
async function apiFetch(path, options = {}) {
  const token = localStorage.getItem("bh_token");

  const res = await fetch(API_BASE + path, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token && { Authorization: "Bearer " + token }),
      ...(options.headers || {})
    }
  });

  const ct = res.headers.get('content-type') || '';

  const body = ct.includes('application/json')
      ? await res.json()
      : await res.text();

  // ✅ 401 (not logged in / expired)
  if (res.status === 401 && Auth.isLoggedIn()) {
    if (!window.location.pathname.includes('login.html')) {
      Auth.clear();
      window.location.href = 'login.html';
    }
    return null;
  }

// ✅ 403 (no permission)
  if (res.status === 403) {
    toast("Error", "error");
    throw new Error("Price must be greater than 0");
  }

  if (!res.ok) {
    const msg = (body && body.message)
        ? body.message
        : `Error ${res.status}`;

    throw new Error(msg);
  }

  return body;
}

async function apiPost(path, data) {
  return apiFetch(path, { method: 'POST', body: JSON.stringify(data) });
}
async function apiPut(path, data) {
  return apiFetch(path, { method: 'PUT', body: JSON.stringify(data) });
}
async function apiPatch(path, data) {
  return apiFetch(path, { method: 'PATCH', body: data !== undefined ? JSON.stringify(data) : undefined });
}
async function apiDelete(path) {
  return apiFetch(path, { method: 'DELETE' });
}

// ─── TOAST ────────────────────────────────────────
function toast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }
  const icons = { info: 'ℹ️', success: '✅', error: '❌', warning: '⚠️' };
  const el = document.createElement('div');
  el.className = `toast ${type}`;
  el.innerHTML = `<span class="toast-icon">${icons[type] || 'ℹ️'}</span><span>${escHtml(message)}</span>`;
  container.appendChild(el);
  setTimeout(() => { el.style.opacity = '0'; el.style.transform = 'translateX(30px)'; el.style.transition = '0.3s'; setTimeout(() => el.remove(), 300); }, 3000);
}

// ─── CONFIRM DIALOG ───────────────────────────────
let _confirmResolve = null;
function showConfirm(title, message) {
  return new Promise(resolve => {
    _confirmResolve = resolve;
    const overlay = document.getElementById('confirm-overlay');
    if (!overlay) { resolve(window.confirm(message)); return; }
    overlay.querySelector('.confirm-title').textContent = title;
    overlay.querySelector('.confirm-msg').textContent = message;
    overlay.classList.add('open');
    _confirmResolve = resolve;
  });
}
function confirmYes() {
  document.getElementById('confirm-overlay').classList.remove('open');
  if (_confirmResolve) { _confirmResolve(true); _confirmResolve = null; }
}
function confirmNo() {
  document.getElementById('confirm-overlay').classList.remove('open');
  if (_confirmResolve) { _confirmResolve(false); _confirmResolve = null; }
}

// ─── UTILITY ──────────────────────────────────────
function escHtml(s) {
  return String(s ?? '')
    .replace(/&/g,'&amp;').replace(/</g,'&lt;')
    .replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}

function formatPrice(n) {
  return '₱' + Number(n || 0).toFixed(2);
}

function formatDate(dt) {
  if (!dt) return '—';
  return new Date(dt).toLocaleDateString('en-PH', { year: 'numeric', month: 'short', day: 'numeric' });
}

function formatDateTime(dt) {
  if (!dt) return '—';
  return new Date(dt).toLocaleString('en-PH', { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
}

function debounce(fn, ms = 350) {
  let t; return (...args) => { clearTimeout(t); t = setTimeout(() => fn(...args), ms); };
}

// ─── NAV RENDERING ────────────────────────────────
function _accountDropdown(user) {
  return `
    <div class="nav-dropdown" id="account-dropdown">
      <button class="nav-link nav-dropdown-toggle" onclick="toggleAccountDropdown(event)">
        Account <span class="nav-dropdown-caret">▾</span>
      </button>
      <div class="nav-dropdown-menu" id="account-menu">
        <div class="nav-dropdown-user">👤 ${escHtml(user)}</div>
        <div class="nav-dropdown-divider"></div>
        <button class="nav-dropdown-item nav-dropdown-signout" onclick="doLogout()">Sign Out</button>
      </div>
    </div>`;
}

function renderNav(activePage) {
  const isLoggedIn = Auth.isLoggedIn();
  const user       = Auth.getUser();

  const navEl = document.getElementById('main-nav');
  if (!navEl) return;

  navEl.innerHTML = `
    <div class="nav-inner">

      <div class="nav-left">
        <a class="nav-brand" href="index.html">Book<span>Haven</span></a>
      </div>

      <div class="nav-right">

        ${isLoggedIn ? `
          <a class="nav-link ${activePage === 'cart' ? 'active' : ''}" href="cart.html">
            Cart
            <span id="cart-badge" class="cart-badge hidden">0</span>
          </a>

          <a class="nav-link ${activePage === 'orders' ? 'active' : ''}" href="orders.html">
            My Orders
          </a>

          ${_accountDropdown(user)}
        ` : `
          <a class="btn btn-secondary btn-sm" href="login.html">Sign In</a>
          <a class="btn btn-primary btn-sm" href="login.html">Register</a>
        `}

      </div>

    </div>
  `;

  if (isLoggedIn) {
    setTimeout(loadCartBadge, 50);
  }
}

function renderAdminNav() {
  const user  = Auth.getUser();
  const navEl = document.getElementById('main-nav');
  if (!navEl) return;

  navEl.innerHTML = `
    <a class="nav-brand" href="index.html">Book<span>Haven</span></a>
    <div class="nav-right">
      ${_accountDropdown(user)}
    </div>`;
}

function toggleAccountDropdown(e) {
  e.stopPropagation();
  const menu = document.getElementById('account-menu');
  if (!menu) return;
  const isOpen = menu.classList.toggle('open');
  if (isOpen) {
    const close = (ev) => {
      if (!document.getElementById('account-dropdown')?.contains(ev.target)) {
        menu.classList.remove('open');
        document.removeEventListener('click', close);
      }
    };
    document.addEventListener('click', close);
  }
}

function doLogout() {
  Auth.clear();
  window.location.href = 'index.html';
}

// ─── CONFIRM HTML (injected if missing) ───────────
function ensureConfirmDialog() {
  if (document.getElementById('confirm-overlay')) return;
  const div = document.createElement('div');
  div.id = 'confirm-overlay';
  div.innerHTML = `
    <div class="confirm-box">
      <h3 class="confirm-title">Confirm Action</h3>
      <p class="confirm-msg">Are you sure?</p>
      <div class="actions">
        <button class="btn btn-ghost" onclick="confirmNo()">Cancel</button>
        <button class="btn btn-danger" onclick="confirmYes()">Confirm</button>
      </div>
    </div>`;
  document.body.appendChild(div);
}

async function loadCartBadge(){
  if(!Auth.isLoggedIn()) return;

  try{
    const res = await apiFetch('/api/cart');
    const count = (res.items || []).reduce((s,i)=> s + i.quantity, 0);

    const badge = document.getElementById('cart-badge');
    if(!badge) return;

    badge.textContent = count;

    if(count > 0){
      badge.classList.add('show');
      badge.classList.remove('hidden');

      // bump animation
      badge.classList.remove('bump');
      void badge.offsetWidth;
      badge.classList.add('bump');
    }else{
      badge.classList.remove('show');
    }

  }catch(e){}
}