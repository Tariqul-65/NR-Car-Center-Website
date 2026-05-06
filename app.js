const navToggle = document.getElementById("navToggle");
const nav = document.getElementById("nav");
if (navToggle && nav) navToggle.addEventListener("click", () => nav.classList.toggle("open"));

document.querySelectorAll("[data-scroll]").forEach((b) => {
  b.addEventListener("click", () => {
    const target = document.querySelector(b.getAttribute("data-scroll"));
    if (target) target.scrollIntoView({ behavior: "smooth", block: "start" });
  });
});

const money = (n) => Number(n || 0).toLocaleString(undefined, { maximumFractionDigits: 0 });

const toast = document.getElementById("toast");
let toastTimer = null;
function showToast(msg) {
  toast.textContent = msg;
  toast.classList.add("show");
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove("show"), 1600);
}

function normalizeText(v) {
  return String(v == null ? "" : v).trim();
}

function safeParseJSON(s, fallback) {
  try {
    return JSON.parse(String(s || ""));
  } catch {
    return fallback;
  }
}

function fileToBase64(file) {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(String(reader.result || ""));
    reader.onerror = reject;
    reader.readAsDataURL(file);
  });
}

async function filesToBase64List(files, maxCount) {
  const list = Array.from(files || []).slice(0, maxCount);
  const out = [];
  for (const f of list) out.push(await fileToBase64(f));
  return out;
}

let stock = [];
let delivered = [];
let auction = [];
let team = [];
let notes = "";

const statStock = document.getElementById("statStock");
const statDelivered = document.getElementById("statDelivered");
const statAuction = document.getElementById("statAuction");
const statTeam = document.getElementById("statTeam");

function updateStats() {
  statStock.textContent = stock.length;
  statDelivered.textContent = delivered.length;
  statAuction.textContent = auction.length;
  statTeam.textContent = team.length;
}

const stockTbody = document.getElementById("stockTbody");
const deliveredTbody = document.getElementById("deliveredTbody");
const auctionTbody = document.getElementById("auctionTbody");
const teamTbody = document.getElementById("teamTbody");

function pillStatus(s) {
  const map = {
    upcoming: { t: "Upcoming", c: "pill-warn" },
    in_stock: { t: "In Stock", c: "pill-ok" },
    reserved: { t: "Reserved", c: "pill-warn" },
    sold: { t: "Sold", c: "pill-bad" },
  };
  const v = map[s] || { t: "Unknown", c: "" };
  return `<span class="pill ${v.c}">${v.t}</span>`;
}

function roleLabel(r) {
  const map = {
    super_admin: "Super Admin",
    manager: "Manager",
    staff: "Staff",
    editor: "Editor",
  };
  return map[r] || "Staff";
}

function memberStatus(s) {
  const map = {
    active: { t: "Active", c: "pill-ok" },
    inactive: { t: "Inactive", c: "pill-bad" },
  };
  const v = map[s] || { t: "Active", c: "pill-ok" };
  return `<span class="pill ${v.c}">${v.t}</span>`;
}

function imgThumb(src) {
  if (!src) return `<span class="img-ph"><i class="fa-regular fa-image"></i></span>`;
  return `<img class="thumb" src="${src}" alt="Image">`;
}

function stockThumbCell(images) {
  const list = Array.isArray(images) ? images : [];
  const first = list[0] || "";
  const count = list.length;
  if (!first) return `<div class="imgcell">${imgThumb("")}<span class="imgcount">0</span></div>`;
  return `<div class="imgcell">${imgThumb(first)}<span class="imgcount">${count}</span></div>`;
}

function renderStock() {
  const q = (document.getElementById("stockSearch").value || "").trim().toLowerCase();
  const st = document.getElementById("stockStatus").value;

  const rows = stock.filter((x) => {
    const hay = `${x.chassis} ${x.make} ${x.model} ${x.year} ${x.color}`.toLowerCase();
    const okQ = !q || hay.includes(q);
    const okS = st === "all" || x.status === st;
    return okQ && okS;
  });

  stockTbody.innerHTML =
    rows
      .map(
        (x, i) => `
        <tr>
          <td>${stockThumbCell(x.images)}</td>
          <td class="mono">${x.chassis}</td>
          <td>${x.make} ${x.model}</td>
          <td>${x.year}</td>
          <td>${x.color}</td>
          <td>${pillStatus(x.status)}</td>
          <td class="right">৳ ${money(x.price)}</td>
          <td class="right">
            <button class="tbtn" type="button" data-act="editStock" data-i="${i}"><i class="fa-solid fa-pen"></i></button>
            <button class="tbtn tbtn-danger" type="button" data-act="delStock" data-i="${i}"><i class="fa-solid fa-trash"></i></button>
          </td>
        </tr>
      `
      )
      .join("") || `<tr><td colspan="8" class="empty">No stock found.</td></tr>`;
}

function renderDelivered() {
  const q = (document.getElementById("deliveredSearch").value || "").trim().toLowerCase();
  const mm = document.getElementById("deliveredMonth").value;

  const rows = delivered.filter((x) => {
    const hay = `${x.chassis} ${x.make} ${x.model} ${x.customer} ${x.date}`.toLowerCase();
    const okQ = !q || hay.includes(q);
    const okM = mm === "all" || (x.date || "").slice(5, 7) === mm;
    return okQ && okM;
  });

  deliveredTbody.innerHTML =
    rows
      .map(
        (x, i) => `
        <tr>
          <td>${imgThumb(x.image)}</td>
          <td class="mono">${x.chassis}</td>
          <td>${x.make} ${x.model}</td>
          <td>${x.customer}</td>
          <td>${x.date}</td>
          <td class="right">৳ ${money(x.cost)}</td>
          <td class="right">
            <button class="tbtn" type="button" data-act="editDelivered" data-i="${i}"><i class="fa-solid fa-pen"></i></button>
            <button class="tbtn tbtn-danger" type="button" data-act="delDelivered" data-i="${i}"><i class="fa-solid fa-trash"></i></button>
          </td>
        </tr>
      `
      )
      .join("") || `<tr><td colspan="7" class="empty">No delivered cars found.</td></tr>`;
}

function renderAuction() {
  const q = (document.getElementById("auctionSearch").value || "").trim().toLowerCase();
  const rows = auction.filter((x) => {
    const hay = `${x.chassis} ${x.make} ${x.model} ${x.auction} ${x.grade}`.toLowerCase();
    return !q || hay.includes(q);
  });

  auctionTbody.innerHTML =
    rows
      .map(
        (x, i) => `
        <tr>
          <td>${imgThumb(x.image)}</td>
          <td class="mono">${x.chassis}</td>
          <td>${x.make} ${x.model}</td>
          <td>${x.auction}</td>
          <td>${x.grade}</td>
          <td class="right">৳ ${money(x.price)}</td>
          <td class="right">
            <button class="tbtn" type="button" data-act="viewAuction" data-i="${i}"><i class="fa-solid fa-eye"></i></button>
            <button class="tbtn" type="button" data-act="editAuction" data-i="${i}"><i class="fa-solid fa-pen"></i></button>
            <button class="tbtn tbtn-danger" type="button" data-act="delAuction" data-i="${i}"><i class="fa-solid fa-trash"></i></button>
          </td>
        </tr>
      `
      )
      .join("") || `<tr><td colspan="7" class="empty">No auction entries found.</td></tr>`;
}

function renderTeam() {
  const q = (document.getElementById("teamSearch").value || "").trim().toLowerCase();
  const role = document.getElementById("teamRole").value;

  const rows = team.filter((x) => {
    const hay = `${x.name} ${x.phone} ${x.email} ${roleLabel(x.role)}`.toLowerCase();
    const okQ = !q || hay.includes(q);
    const okR = role === "all" || x.role === role;
    return okQ && okR;
  });

  teamTbody.innerHTML =
    rows
      .map(
        (x, i) => `
        <tr>
          <td>${imgThumb(x.photo)}</td>
          <td>${x.name}</td>
          <td class="mono">${x.phone}</td>
          <td>${x.email}</td>
          <td>${roleLabel(x.role)}</td>
          <td>${memberStatus(x.status)}</td>
          <td class="right">
            <button class="tbtn" type="button" data-act="editTeam" data-i="${i}"><i class="fa-solid fa-pen"></i></button>
            <button class="tbtn tbtn-danger" type="button" data-act="delTeam" data-i="${i}"><i class="fa-solid fa-trash"></i></button>
          </td>
        </tr>
      `
      )
      .join("") || `<tr><td colspan="7" class="empty">No members found.</td></tr>`;
}

function renderAll() {
  renderStock();
  renderDelivered();
  renderAuction();
  renderTeam();
}

const modal = document.getElementById("modal");
const modalTitle = document.getElementById("modalTitle");
const modalFields = document.getElementById("modalFields");
const modalForm = document.getElementById("modalForm");
const closeModal = document.getElementById("closeModal");
const cancelModal = document.getElementById("cancelModal");

let modalMode = null;
let editIndex = -1;

function openModal(title, mode, fields, values) {
  modalTitle.textContent = title;
  modalMode = mode;

  modalFields.innerHTML = fields
    .map((f) => {
      const v = values && values[f.name] != null ? values[f.name] : "";

      if (f.type === "select") {
        const opts = f.options
          .map((o) => `<option value="${o.value}" ${String(v) === String(o.value) ? "selected" : ""}>${o.label}</option>`)
          .join("");
        return `
          <div class="mfield ${f.full ? "full" : ""}">
            <label>${f.label}</label>
            <select name="${f.name}" ${f.req ? "required" : ""}>
              ${f.req ? `<option value="" ${v === "" ? "selected" : ""} disabled>Select</option>` : ""}
              ${opts}
            </select>
          </div>
        `;
      }

      if (f.type === "textarea") {
        return `
          <div class="mfield ${f.full ? "full" : ""}">
            <label>${f.label}</label>
            <textarea name="${f.name}" placeholder="${f.ph || ""}" ${f.req ? "required" : ""}>${String(v || "")}</textarea>
          </div>
        `;
      }

      if (f.type === "file_single") {
        const has = values && values[f.previewKey];
        return `
          <div class="mfield ${f.full ? "full" : ""}">
            <label>${f.label}</label>
            <input name="${f.name}" type="file" accept="image/*">
            <input type="hidden" name="${f.previewKey}" value="${has ? String(values[f.previewKey]).replaceAll('"', "&quot;") : ""}">
            <div class="img-preview">${has ? `<img src="${values[f.previewKey]}" alt="Preview">` : `<span class="muted">No image selected</span>`}</div>
          </div>
        `;
      }

      if (f.type === "file_multi") {
        const hasList = values && Array.isArray(values[f.previewKey]) ? values[f.previewKey] : [];
        const json = JSON.stringify(hasList || []);
        const previews = (hasList || []).map((src) => `<img src="${src}" alt="Preview">`).join("");
        return `
          <div class="mfield full">
            <label>${f.label}</label>
            <input name="${f.name}" type="file" accept="image/*" multiple>
            <input type="hidden" name="${f.previewKey}_json" value="${json.replaceAll('"', "&quot;")}">
            <div class="img-preview-grid">
              ${previews || `<span class="muted">No images selected</span>`}
            </div>
            <div class="hint">Max ${f.max || 10} images</div>
          </div>
        `;
      }

      return `
        <div class="mfield ${f.full ? "full" : ""}">
          <label>${f.label}</label>
          <input name="${f.name}" type="${f.type}" placeholder="${f.ph || ""}"
                 value="${String(v || "").replaceAll('"', "&quot;")}" ${f.req ? "required" : ""}>
        </div>
      `;
    })
    .join("");

  modal.classList.add("open");

  const single = modalFields.querySelector('input[type="file"]:not([multiple])');
  if (single) {
    single.addEventListener("change", async () => {
      const f = single.files && single.files[0];
      const hidden = modalFields.querySelector(`input[name="image_file_base64"], input[name="photo_base64"]`);
      const prev = modalFields.querySelector(".img-preview");
      if (!f) {
        if (prev) prev.innerHTML = `<span class="muted">No image selected</span>`;
        return;
      }
      const b64 = await fileToBase64(f);
      if (hidden) hidden.value = b64;
      if (prev) prev.innerHTML = `<img src="${b64}" alt="Preview">`;
    });
  }

  const multi = modalFields.querySelector('input[type="file"][multiple]');
  if (multi) {
    multi.addEventListener("change", async () => {
      const files = multi.files || [];
      const list = await filesToBase64List(files, 10);
      const hidden = modalFields.querySelector(`input[name="images_json"]`);
      const prev = modalFields.querySelector(".img-preview-grid");
      if (hidden) hidden.value = JSON.stringify(list || []);
      if (prev) prev.innerHTML = list.length ? list.map((src) => `<img src="${src}" alt="Preview">`).join("") : `<span class="muted">No images selected</span>`;
    });
  }
}

function closeModalFn() {
  modal.classList.remove("open");
  modalMode = null;
  editIndex = -1;
  modalForm.reset();
}

closeModal.addEventListener("click", closeModalFn);
cancelModal.addEventListener("click", closeModalFn);
modal.addEventListener("click", (e) => {
  if (e.target === modal) closeModalFn();
});

function getFormData(form) {
  const fd = new FormData(form);
  const obj = {};
  fd.forEach((v, k) => (obj[k] = v));
  return obj;
}

const stockFields = [
  { name: "images_files", label: "Car Images", type: "file_multi", previewKey: "images", max: 10 },
  { name: "chassis", label: "Chassis No", type: "text", ph: "e.g. ABC12345", req: true },
  { name: "make", label: "Make", type: "text", ph: "e.g. Toyota", req: true },
  { name: "model", label: "Model", type: "text", ph: "e.g. Premio", req: true },
  { name: "year", label: "Year", type: "number", ph: "e.g. 2018", req: true },
  { name: "color", label: "Color", type: "text", ph: "e.g. Pearl White", req: true },
  { name: "status", label: "Status", type: "select", req: true, options: [
    { value: "upcoming", label: "Upcoming" },
    { value: "in_stock", label: "In Stock" },
    { value: "reserved", label: "Reserved" },
    { value: "sold", label: "Sold" },
  ]},
  { name: "price", label: "Price (BDT)", type: "number", ph: "e.g. 2500000", req: true },
];

const deliveredFields = [
  { name: "image_file", label: "Car Image", type: "file_single", previewKey: "image_file_base64", full: true },
  { name: "chassis", label: "Chassis No", type: "text", ph: "e.g. ABC12345", req: true },
  { name: "make", label: "Make", type: "text", ph: "e.g. Toyota", req: true },
  { name: "model", label: "Model", type: "text", ph: "e.g. Axio", req: true },
  { name: "customer", label: "Customer Name", type: "text", ph: "e.g. Rahim", req: true },
  { name: "date", label: "Delivery Date", type: "date", req: true },
  { name: "cost", label: "Delivery Cost (BDT)", type: "number", ph: "e.g. 15000", req: true },
];

const auctionFields = [
  { name: "image_file", label: "Car Image", type: "file_single", previewKey: "image_file_base64", full: true },
  { name: "chassis", label: "Chassis No", type: "text", ph: "e.g. ABC12345", req: true },
  { name: "make", label: "Make", type: "text", ph: "e.g. Toyota", req: true },
  { name: "model", label: "Model", type: "text", ph: "e.g. Harrier", req: true },
  { name: "year", label: "Year", type: "number", ph: "e.g. 2019", req: true },
  { name: "color", label: "Color", type: "text", ph: "e.g. Black", req: true },
  { name: "mileage", label: "Mileage", type: "text", ph: "e.g. 62,000 km", req: false },
  { name: "grade", label: "Grade", type: "text", ph: "e.g. 4.5", req: true },
  { name: "auction", label: "Auction House", type: "text", ph: "e.g. USS Tokyo", req: true },
  { name: "price", label: "Auction Price (BDT)", type: "number", ph: "e.g. 3800000", req: true },
  { name: "notes", label: "Details / Notes", type: "textarea", ph: "Add extra details...", full: true, req: false },
];

const teamFields = [
  { name: "photo_file", label: "Profile Photo", type: "file_single", previewKey: "photo_base64", full: true },
  { name: "name", label: "Full Name", type: "text", ph: "Enter full name", req: true },
  { name: "phone", label: "Phone", type: "tel", ph: "+88017XXXXXXXX", req: true },
  { name: "email", label: "Email", type: "email", ph: "name@nrcarcenter.com", req: true },
  { name: "role", label: "Role", type: "select", req: true, options: [
    { value: "super_admin", label: "Super Admin" },
    { value: "manager", label: "Manager" },
    { value: "staff", label: "Staff" },
    { value: "editor", label: "Editor" },
  ]},
  { name: "status", label: "Status", type: "select", req: true, options: [
    { value: "active", label: "Active" },
    { value: "inactive", label: "Inactive" },
  ]},
];

document.getElementById("openAddStock").addEventListener("click", () => {
  editIndex = -1;
  openModal("Add Stock Car", "addStock", stockFields, { status: "in_stock", images: [] });
  const hidden = modalFields.querySelector(`input[name="images_json"]`);
  if (hidden) hidden.value = JSON.stringify([]);
});

document.getElementById("openAddDelivered").addEventListener("click", () => {
  editIndex = -1;
  openModal("Add Delivered Car", "addDelivered", deliveredFields, {});
});

document.getElementById("openAddAuction").addEventListener("click", () => {
  editIndex = -1;
  openModal("Add Auction Entry", "addAuction", auctionFields, {});
});

document.getElementById("openAddMember").addEventListener("click", () => {
  editIndex = -1;
  openModal("Add Team Member", "addTeam", teamFields, { status: "active", role: "staff" });
});

function mapStockData(data) {
  const images = safeParseJSON(data.images_json, []);
  return {
    images: Array.isArray(images) ? images.slice(0, 10) : [],
    chassis: normalizeText(data.chassis),
    make: normalizeText(data.make),
    model: normalizeText(data.model),
    year: normalizeText(data.year),
    color: normalizeText(data.color),
    status: normalizeText(data.status),
    price: normalizeText(data.price),
  };
}

function mapDeliveredData(data) {
  return {
    image: normalizeText(data.image_file_base64),
    chassis: normalizeText(data.chassis),
    make: normalizeText(data.make),
    model: normalizeText(data.model),
    customer: normalizeText(data.customer),
    date: normalizeText(data.date),
    cost: normalizeText(data.cost),
  };
}

function mapAuctionData(data) {
  return {
    image: normalizeText(data.image_file_base64),
    chassis: normalizeText(data.chassis),
    make: normalizeText(data.make),
    model: normalizeText(data.model),
    year: normalizeText(data.year),
    color: normalizeText(data.color),
    mileage: normalizeText(data.mileage),
    grade: normalizeText(data.grade),
    auction: normalizeText(data.auction),
    price: normalizeText(data.price),
    notes: normalizeText(data.notes),
  };
}

function mapTeamData(data) {
  return {
    photo: normalizeText(data.photo_base64),
    name: normalizeText(data.name),
    phone: normalizeText(data.phone),
    email: normalizeText(data.email),
    role: normalizeText(data.role),
    status: normalizeText(data.status),
  };
}

modalForm.addEventListener("submit", (e) => {
  e.preventDefault();
  const data = getFormData(modalForm);

  if (modalMode === "addStock") stock.unshift(mapStockData(data));
  if (modalMode === "editStock") stock[editIndex] = { ...stock[editIndex], ...mapStockData(data) };

  if (modalMode === "addDelivered") delivered.unshift(mapDeliveredData(data));
  if (modalMode === "editDelivered") delivered[editIndex] = { ...delivered[editIndex], ...mapDeliveredData(data) };

  if (modalMode === "addAuction") auction.unshift(mapAuctionData(data));
  if (modalMode === "editAuction") auction[editIndex] = { ...auction[editIndex], ...mapAuctionData(data) };

  if (modalMode === "addTeam") team.unshift(mapTeamData(data));
  if (modalMode === "editTeam") team[editIndex] = { ...team[editIndex], ...mapTeamData(data) };

  closeModalFn();
  updateStats();
  renderAll();
  showToast("Saved");
});

function findByChassis(ch) {
  const q = (ch || "").trim().toLowerCase();
  if (!q) return null;
  const a = auction.find((x) => (x.chassis || "").toLowerCase() === q);
  const s = stock.find((x) => (x.chassis || "").toLowerCase() === q);
  const d = delivered.find((x) => (x.chassis || "").toLowerCase() === q);
  return { auction: a || null, stock: s || null, delivered: d || null };
}

const chassisResult = document.getElementById("chassisResult");
function renderChassisResult(q) {
  const found = findByChassis(q);
  const a = found && found.auction;
  const s = found && found.stock;
  const d = found && found.delivered;

  if (!q || !q.trim()) {
    chassisResult.innerHTML = `
      <div class="result-empty">
        <i class="fa-solid fa-circle-info"></i>
        <div>
          <h3>No chassis number</h3>
          <p>Enter chassis number to search.</p>
        </div>
      </div>
    `;
    return;
  }

  if (!a && !s && !d) {
    chassisResult.innerHTML = `
      <div class="result-empty">
        <i class="fa-solid fa-triangle-exclamation"></i>
        <div>
          <h3>Not found</h3>
          <p>No data found for <span class="mono">${q}</span>.</p>
        </div>
      </div>
    `;
    return;
  }

  const img = (a && a.image) || (d && d.image) || (s && Array.isArray(s.images) ? (s.images[0] || "") : "") || "";
  const carName = (a && `${a.make} ${a.model}`) || (s && `${s.make} ${s.model}`) || (d && `${d.make} ${d.model}`) || "Car";
  const year = (a && a.year) || (s && s.year) || "";
  const color = (a && a.color) || (s && s.color) || "";
  const auctionPrice = a && a.price ? `৳ ${money(a.price)}` : "—";
  const auctionHouse = (a && a.auction) || "—";
  const grade = (a && a.grade) || "—";
  const mileage = (a && a.mileage) || "—";
  const note = a && a.notes ? a.notes : "";

  const stockStatus = s ? pillStatus(s.status) : `<span class="pill">No Stock Info</span>`;
  const stockPrice = s && s.price ? `৳ ${money(s.price)}` : "—";

  const delInfo = d ? `<span class="pill pill-ok">Delivered</span>` : `<span class="pill">Not Delivered</span>`;
  const delDate = d && d.date ? d.date : "—";
  const delCost = d && d.cost ? `৳ ${money(d.cost)}` : "—";
  const customer = d && d.customer ? d.customer : "—";

  const imgHtml = img
    ? `<div class="bigimg"><img src="${img}" alt="Car"></div>`
    : `<div class="bigimg bigimg-empty"><i class="fa-regular fa-image"></i><span>No image</span></div>`;

  chassisResult.innerHTML = `
    <div class="result-top">
      <div>
        <h3>${carName}</h3>
        <p class="muted"><span class="mono">${q}</span> ${year ? `• ${year}` : ""} ${color ? `• ${color}` : ""}</p>
      </div>
      <div class="result-badges">
        ${delInfo}
        ${stockStatus}
      </div>
    </div>

    ${imgHtml}

    <div class="result-grid">
      <div class="rbox">
        <h4>Auction Info</h4>
        <div class="kv"><span>Auction</span><b>${auctionHouse}</b></div>
        <div class="kv"><span>Grade</span><b>${grade}</b></div>
        <div class="kv"><span>Mileage</span><b>${mileage}</b></div>
        <div class="kv"><span>Price</span><b>${auctionPrice}</b></div>
      </div>

      <div class="rbox">
        <h4>Stock Info</h4>
        <div class="kv"><span>Status</span><b>${s ? (s.status === "in_stock" ? "In Stock" : s.status === "reserved" ? "Reserved" : s.status === "sold" ? "Sold" : "Upcoming") : "—"}</b></div>
        <div class="kv"><span>Showroom Price</span><b>${stockPrice}</b></div>
      </div>

      <div class="rbox">
        <h4>Delivered Info</h4>
        <div class="kv"><span>Customer</span><b>${customer}</b></div>
        <div class="kv"><span>Delivery Date</span><b>${delDate}</b></div>
        <div class="kv"><span>Delivery Cost</span><b>${delCost}</b></div>
      </div>
    </div>

    ${note ? `<div class="note"><b>Details:</b> ${note}</div>` : ``}
  `;
}

const chassisQuery = document.getElementById("chassisQuery");
document.getElementById("searchChassisBtn").addEventListener("click", () => renderChassisResult(chassisQuery.value));
chassisQuery.addEventListener("keydown", (e) => {
  if (e.key === "Enter") {
    e.preventDefault();
    renderChassisResult(chassisQuery.value);
  }
});

document.getElementById("stockSearch").addEventListener("input", renderStock);
document.getElementById("stockStatus").addEventListener("change", renderStock);
document.getElementById("clearStockFilters").addEventListener("click", () => {
  document.getElementById("stockSearch").value = "";
  document.getElementById("stockStatus").value = "all";
  renderStock();
});

document.getElementById("deliveredSearch").addEventListener("input", renderDelivered);
document.getElementById("deliveredMonth").addEventListener("change", renderDelivered);
document.getElementById("clearDeliveredFilters").addEventListener("click", () => {
  document.getElementById("deliveredSearch").value = "";
  document.getElementById("deliveredMonth").value = "all";
  renderDelivered();
});

document.getElementById("auctionSearch").addEventListener("input", renderAuction);

document.getElementById("teamSearch").addEventListener("input", renderTeam);
document.getElementById("teamRole").addEventListener("change", renderTeam);
document.getElementById("clearTeamFilters").addEventListener("click", () => {
  document.getElementById("teamSearch").value = "";
  document.getElementById("teamRole").value = "all";
  renderTeam();
});

document.addEventListener("click", (e) => {
  const btn = e.target.closest("[data-act]");
  if (!btn) return;
  const act = btn.getAttribute("data-act");
  const i = Number(btn.getAttribute("data-i"));

  if (act === "delStock") {
    stock.splice(i, 1);
    updateStats();
    renderStock();
    showToast("Deleted");
  }
  if (act === "editStock") {
    editIndex = i;
    openModal("Edit Stock Car", "editStock", stockFields, { ...stock[i], images: Array.isArray(stock[i].images) ? stock[i].images : [] });
    const hidden = modalFields.querySelector(`input[name="images_json"]`);
    if (hidden) hidden.value = JSON.stringify(Array.isArray(stock[i].images) ? stock[i].images : []);
  }

  if (act === "delDelivered") {
    delivered.splice(i, 1);
    updateStats();
    renderDelivered();
    showToast("Deleted");
  }
  if (act === "editDelivered") {
    editIndex = i;
    openModal("Edit Delivered Car", "editDelivered", deliveredFields, { ...delivered[i], image_file_base64: delivered[i].image || "" });
  }

  if (act === "delAuction") {
    auction.splice(i, 1);
    updateStats();
    renderAuction();
    showToast("Deleted");
  }
  if (act === "editAuction") {
    editIndex = i;
    openModal("Edit Auction Entry", "editAuction", auctionFields, { ...auction[i], image_file_base64: auction[i].image || "" });
  }
  if (act === "viewAuction") {
    const a = auction[i];
    chassisQuery.value = a.chassis || "";
    renderChassisResult(chassisQuery.value);
    document.getElementById("chassis").scrollIntoView({ behavior: "smooth", block: "start" });
  }

  if (act === "delTeam") {
    team.splice(i, 1);
    updateStats();
    renderTeam();
    showToast("Deleted");
  }
  if (act === "editTeam") {
    editIndex = i;
    openModal("Edit Team Member", "editTeam", teamFields, { ...team[i], photo_base64: team[i].photo || "" });
  }
});

const notesBox = document.getElementById("adminNotes");
document.getElementById("saveNotes").addEventListener("click", () => {
  notes = notesBox.value || "";
  showToast("Saved");
});

document.getElementById("resetAllData").addEventListener("click", () => {
  stock = [];
  delivered = [];
  auction = [];
  team = [];
  notes = "";
  notesBox.value = "";
  chassisQuery.value = "";
  renderChassisResult("");
  updateStats();
  renderAll();
  showToast("Reset");
});

updateStats();
renderAll();
