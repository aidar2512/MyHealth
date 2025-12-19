/**
 * Super-simple frontend without React/Vite:
 * - index.html + styles.css + app.js
 * - served by Spring Boot from / (static resources)
 */

const API = ""; // same origin (http://localhost:8080)

const els = {
  authCard: byId("authCard"),
  dash: byId("dash"),

  tabLogin: byId("tabLogin"),
  tabRegister: byId("tabRegister"),
  authTitle: byId("authTitle"),
  registerFields: byId("registerFields"),

  email: byId("email"),
  password: byId("password"),
  fullName: byId("fullName"),
  age: byId("age"),
  gender: byId("gender"),
  submitAuth: byId("submitAuth"),
  authMsg: byId("authMsg"),

  userBadge: byId("userBadge"),
  logoutBtn: byId("logoutBtn"),

  metricType: byId("metricType"),
  metricValue: byId("metricValue"),
  saveEntry: byId("saveEntry"),
  saveMsg: byId("saveMsg"),

  analysisList: byId("analysisList"),
  recsList: byId("recsList"),
  notifList: byId("notifList"),
  entriesBody: document.querySelector("#entriesTable tbody"),
};

let mode = "login"; // "login" | "register"

function byId(id){ return document.getElementById(id); }

function token(){ return localStorage.getItem("token"); }
function setToken(t){ localStorage.setItem("token", t); }
function clearToken(){ localStorage.removeItem("token"); }

async function api(path, init = {}){
  const headers = { "Content-Type": "application/json", ...(init.headers || {}) };
  if (token()) headers["Authorization"] = "Bearer " + token();

  const res = await fetch(API + path, { ...init, headers });

  const raw = await res.text().catch(() => "");
  let data = null;
  try { data = raw ? JSON.parse(raw) : null; } catch { /* ignore */ }

  if (!res.ok){
    const msg = (data && data.message) ? data.message : (raw || ("HTTP " + res.status));
    throw new Error(msg);
  }
  return data;
}

function show(el){ el.classList.remove("hidden"); }
function hide(el){ el.classList.add("hidden"); }

function setMode(m){
  mode = m;
  if (mode === "login"){
    els.authTitle.textContent = "Login";
    els.tabLogin.classList.add("active");
    els.tabRegister.classList.remove("active");
    hide(els.registerFields);
  } else {
    els.authTitle.textContent = "Register";
    els.tabRegister.classList.add("active");
    els.tabLogin.classList.remove("active");
    show(els.registerFields);
  }
  els.authMsg.textContent = "";
}

els.tabLogin.addEventListener("click", () => setMode("login"));
els.tabRegister.addEventListener("click", () => setMode("register"));

els.logoutBtn.addEventListener("click", () => {
  clearToken();
  location.reload();
});

els.submitAuth.addEventListener("click", async () => {
  els.authMsg.textContent = "";
  try{
    const email = els.email.value.trim();
    const password = els.password.value;
    if (!email || !password) throw new Error("Fill email and password");
    let r;
    if (mode === "login"){
      r = await api("/api/auth/login", { method:"POST", body: JSON.stringify({ email, password })});
    } else {
      const fullName = els.fullName.value.trim();
      const age = els.age.value ? Number(els.age.value) : null;
      const gender = els.gender.value;
      if (!fullName) throw new Error("Fill full name");
      r = await api("/api/auth/register", { method:"POST", body: JSON.stringify({ email, password, fullName, age, gender })});
    }
    setToken(r.token);
    await initAuthed();
  } catch(e){
    els.authMsg.textContent = (e && e.message) ? e.message : "Error";
  }
});

els.saveEntry.addEventListener("click", async () => {
  els.saveMsg.textContent = "";
  try{
    const type = els.metricType.value;

    const raw = els.metricValue.value.replace(",", ".");
    const value = Number(raw);

    if (!Number.isFinite(value)) throw new Error("Enter a number value");
    await api("/api/health-data", { method:"POST", body: JSON.stringify({ type, value, source: "manual" })});
    els.metricValue.value = "";
    els.saveMsg.textContent = "Saved!";
    await refresh();
  } catch(e){
    els.saveMsg.textContent = (e && e.message) ? e.message : "Error";
  }
});

function metricLabel(type){
  const map = {
    BLOOD_PRESSURE_SYSTOLIC: "Blood pressure (systolic)",
    BLOOD_PRESSURE_DIASTOLIC: "Blood pressure (diastolic)",
    GLUCOSE: "Glucose",
    PULSE: "Pulse",
  };
  return map[type] || type;
}

function renderAnalysis(analysis){
  if (!analysis || analysis.length === 0){
    els.analysisList.classList.add("muted");
    els.analysisList.textContent = "No data yet.";
    return;
  }
  els.analysisList.classList.remove("muted");
  els.analysisList.innerHTML = "";
  analysis.forEach(a => {
    const div = document.createElement("div");
    div.className = "item";
    div.innerHTML = `
      <div style="display:flex; justify-content:space-between; gap:12px; align-items:flex-start;">
        <div>
          <div><b>${metricLabel(a.type)}</b></div>
          <small class="muted">${a.message}</small>
        </div>
        <div style="text-align:right">
          <span class="pill ${a.status}">${a.status}</span><br/>
          <small class="muted">${a.value}</small>
        </div>
      </div>
    `;
    els.analysisList.appendChild(div);
  });
}

function renderRecs(recs){
  els.recsList.innerHTML = "";
  (recs || []).forEach(r => {
    const li = document.createElement("li");
    li.textContent = r;
    els.recsList.appendChild(li);
  });
}

function renderNotifs(notifs){
  if (!notifs || notifs.length === 0){
    els.notifList.classList.add("muted");
    els.notifList.textContent = "No notifications.";
    return;
  }
  els.notifList.classList.remove("muted");
  els.notifList.innerHTML = "";
  notifs.slice(0,6).forEach(n => {
    const div = document.createElement("div");
    div.className = "item";
    const dt = new Date(n.createdAt).toLocaleString();
    div.innerHTML = `
      <div style="display:flex; justify-content:space-between; gap:12px;">
        <b>${n.title}</b>
        <small class="muted">${dt}</small>
      </div>
      <small class="muted">${n.message}</small>
    `;
    els.notifList.appendChild(div);
  });
}

function renderEntries(entries){
  els.entriesBody.innerHTML = "";
  (entries || []).slice(0, 10).forEach(e => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${new Date(e.recordedAt).toLocaleString()}</td>
      <td>${metricLabel(e.type)}</td>
      <td>${e.value}</td>
      <td class="muted">${e.source}</td>
    `;
    els.entriesBody.appendChild(tr);
  });
}

async function refresh(){
  const [entries, report, recs, notifs] = await Promise.all([
    api("/api/health-data/latest"),
    api("/api/reports/summary"),
    api("/api/recommendations/latest"),
    api("/api/notifications")
  ]);
  renderEntries(entries);
  renderAnalysis(report.analysis);
  renderRecs(recs.recommendations);
  renderNotifs(notifs);
}

async function initAuthed(){
  hide(els.authCard);
  show(els.dash);
  show(els.logoutBtn);
  try{
    const me = await api("/api/profile");
    els.userBadge.textContent = me.fullName;
    show(els.userBadge);
  } catch { /* ignore */ }
  await refresh();
}

async function boot(){
  setMode("login");
  if (token()){
    try{
      await initAuthed();
      return;
    } catch {
      clearToken();
    }
  }
  show(els.authCard);
  hide(els.dash);
  hide(els.logoutBtn);
  hide(els.userBadge);
}

boot();
