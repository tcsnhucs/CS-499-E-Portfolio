// Global state
let allAnimals = [];
let filteredAnimals = [];
let currentPage = 0;
let itemsPerPage = 10;
let selectedRowIndex = 0;
let searchText = '';
let map = null;
let marker = null;
let sortKey = null;
let sortOrder = 1; // 1 = ascending (start ascending first), -1 = descending

// Initialize the app
async function init() {
  await loadAnimals('All');
  initializeEventListeners();
  initializeMap();
  renderChart();
  renderTable();
}

// Load animals from API
async function loadAnimals(filterType) {
  try {
    const response = await fetch(`/api/animals?filter=${filterType}`);
    allAnimals = await response.json();
    filteredAnimals = [...allAnimals];
    currentPage = 0;
    selectedRowIndex = 0;
    searchText = '';
    document.getElementById('searchInput').value = '';
  } catch (error) {
    console.error('Error loading animals:', error);
    document.querySelector('.table-section').innerHTML = 
      '<p class="loading">Error loading data. Please refresh the page.</p>';
  }
}

// Initialize event listeners
function initializeEventListeners() {
  // Filter radio buttons
  document.querySelectorAll('input[name="filterType"]').forEach(radio => {
    radio.addEventListener('change', async (e) => {
      await loadAnimals(e.target.value);
      renderChart();
      updateMapLocation();
      renderTable();
    });
  });

  // Search input
  document.getElementById('searchInput').addEventListener('input', (e) => {
    searchText = e.target.value.toLowerCase();
    currentPage = 0;
    applySearch();
    renderTable();
  });
}

// Apply search filter
function applySearch() {
  if (searchText === '') {
    filteredAnimals = [...allAnimals];
  } else {
    filteredAnimals = allAnimals.filter(animal =>
      Object.values(animal).some(val =>
        String(val).toLowerCase().includes(searchText)
      )
    );
  }
}

// Render the data table
function renderTable() {
  if (allAnimals.length === 0) {
    document.querySelector('.table-section').innerHTML = 
      '<h3>Animal Data Table</h3><p class="loading">No animals found matching your criteria.</p>';
    return;
  }

  // Create table header with sortable columns
  const tableHead = document.getElementById('tableHead');
  tableHead.innerHTML = '';
  // Apply search so header/columns reflect current filtered set
  applySearch();

  if (filteredAnimals.length > 0) {
    const headerRow = document.createElement('tr');
    Object.keys(filteredAnimals[0]).forEach(key => {
      const th = document.createElement('th');
      th.textContent = key;
      th.style.cursor = 'pointer';
      th.addEventListener('click', () => {
        // Toggle sort: first click -> ascending, next -> descending
        if (sortKey === key) {
          sortOrder = -sortOrder;
        } else {
          sortKey = key;
          sortOrder = 1; // start with ascending
        }
        // Apply sort and re-render table from first page
        currentPage = 0;
        applySorting();
        renderTable();
      });
      headerRow.appendChild(th);
    });
    tableHead.appendChild(headerRow);
  }

  // Ensure sorting is applied to the current filtered set before pagination
  applySorting();

  // Paginate data
  const start = currentPage * itemsPerPage;
  const end = start + itemsPerPage;
  const paginatedData = filteredAnimals.slice(start, end);

  // Create table body
  const tableBody = document.getElementById('tableBody');
  tableBody.innerHTML = '';
  paginatedData.forEach((animal, idx) => {
    const row = document.createElement('tr');
    const actualIndex = start + idx;
    if (idx === selectedRowIndex && currentPage === 0) row.classList.add('selected');
    
    row.addEventListener('click', () => {
      selectedRowIndex = idx;
      document.querySelectorAll('#tableBody tr').forEach(r => r.classList.remove('selected'));
      row.classList.add('selected');
      updateMapLocation();
    });

    Object.values(animal).forEach(val => {
      const td = document.createElement('td');
      td.textContent = String(val).substring(0, 50);
      row.appendChild(td);
    });
    tableBody.appendChild(row);
  });

  // Pagination (single input jump)
  const totalPages = Math.max(1, Math.ceil(filteredAnimals.length / itemsPerPage));
  renderPagination(totalPages);

  // Update the chart to reflect current filtered results
  renderChart();

  // Table info
  document.getElementById('tableInfo').textContent = 
    `Page ${currentPage + 1} of ${totalPages} | Total: ${filteredAnimals.length} records`;
}

// Render pagination buttons
function renderPagination(totalPages) {
  const pagination = document.getElementById('pagination');
  pagination.innerHTML = '';
  // First / Previous buttons
  const firstBtn = document.createElement('button');
  firstBtn.textContent = '« First';
  firstBtn.disabled = currentPage <= 0;
  firstBtn.addEventListener('click', () => { currentPage = 0; renderTable(); });
  const prevBtn = document.createElement('button');
  prevBtn.textContent = '‹ Prev';
  prevBtn.disabled = currentPage <= 0;
  prevBtn.addEventListener('click', () => { currentPage = Math.max(0, currentPage - 1); renderTable(); });

  // Page input and Go
  const label = document.createElement('span');
  label.textContent = ` Page (1-${totalPages}): `;
  const input = document.createElement('input');
  input.type = 'number';
  input.min = 1;
  input.max = totalPages;
  input.value = Math.min(totalPages, Math.max(1, currentPage + 1));
  input.style.width = '80px';
  input.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') {
      const v = Number(input.value);
      if (!isNaN(v)) {
        currentPage = Math.min(Math.max(0, v - 1), totalPages - 1);
        renderTable();
      }
    }
  });
  const go = document.createElement('button');
  go.textContent = 'Go';
  go.addEventListener('click', () => {
    const v = Number(input.value);
    if (!isNaN(v)) {
      currentPage = Math.min(Math.max(0, v - 1), totalPages - 1);
      renderTable();
    }
  });

  // Next / Last buttons
  const nextBtn = document.createElement('button');
  nextBtn.textContent = 'Next ›';
  nextBtn.disabled = currentPage >= totalPages - 1;
  nextBtn.addEventListener('click', () => { currentPage = Math.min(totalPages - 1, currentPage + 1); renderTable(); });
  const lastBtn = document.createElement('button');
  lastBtn.textContent = 'Last »';
  lastBtn.disabled = currentPage >= totalPages - 1;
  lastBtn.addEventListener('click', () => { currentPage = totalPages - 1; renderTable(); });

  pagination.appendChild(firstBtn);
  pagination.appendChild(prevBtn);
  pagination.appendChild(label);
  pagination.appendChild(input);
  pagination.appendChild(go);
  pagination.appendChild(nextBtn);
  pagination.appendChild(lastBtn);
}

// Apply sorting to filteredAnimals in place
function applySorting() {
  if (!sortKey) return;
  filteredAnimals.sort((a, b) => {
    const va = a[sortKey] ?? '';
    const vb = b[sortKey] ?? '';
    // Compare as numbers when possible
    const na = parseFloat(va);
    const nb = parseFloat(vb);
    if (!isNaN(na) && !isNaN(nb)) {
      return (na - nb) * sortOrder;
    }
    // Fallback to localeCompare for strings (ascending when sortOrder=1)
    return String(va).localeCompare(String(vb), undefined, { numeric: true }) * sortOrder;
  });
}

// Render breed chart
function renderChart() {
  // Use filteredAnimals so chart reflects current filters/search
  const breedCounts = {};
  const dataSource = filteredAnimals.length > 0 ? filteredAnimals : allAnimals;
  dataSource.forEach(animal => {
    const breed = animal.breed || 'Unknown';
    breedCounts[breed] = (breedCounts[breed] || 0) + 1;
  });

  const total = Object.values(breedCounts).reduce((s, v) => s + v, 0) || 1;

  // Separate breeds into >=.75% and <.75% (Other)
  const major = [];
  let otherCount = 0;
  Object.entries(breedCounts).forEach(([breed, count]) => {
    if (count / total >= 0.0075) {
      major.push({ breed, count });
    } else {
      otherCount += count;
    }
  });

  major.sort((a, b) => b.count - a.count);

  const labels = major.map(d => d.breed);
  const values = major.map(d => d.count);
  if (otherCount > 0) {
    labels.push('Other');
    values.push(otherCount);
  }

  const pieData = [{
    labels,
    values,
    type: 'pie'
  }];

  const layout = {
    height: 500,
    margin: { t: 40, b: 40, l: 40, r: 40 },
    paper_bgcolor: 'rgba(0,0,0,0)',
    plot_bgcolor: 'rgba(0,0,0,0)',
  };

  const plotConfig = {
    responsive: true,
    displaylogo: false,
    modeBarButtonsToRemove: ['toImage', 'zoom2d', 'pan2d', 'select2d', 'lasso2d', 'zoomIn2d', 'zoomOut2d', 'resetScale2d']
  };

  Plotly.newPlot('breedChart', pieData, layout, plotConfig);
}

// Initialize map
function initializeMap() {
  // Initialize map without the default Leaflet attribution;
  map = L.map('map', { attributionControl: false }).setView([30.75, -97.48], 10);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
    maxZoom: 19
  }).addTo(map);
}

// Update map location
function updateMapLocation() {
  if (!map) return;
  if (allAnimals.length === 0) return;

  const paginatedData = filteredAnimals.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );
  const animal = paginatedData[selectedRowIndex] || filteredAnimals[0] || allAnimals[0];

  if (!animal) return;

  const lat = parseFloat(animal.location_lat) || 30.75;
  const lng = parseFloat(animal.location_long) || -97.48;
  const breed = animal.breed || 'Unknown';
  const name = animal.animal_id || 'Unknown';

  // Update animal info
  document.getElementById('selectedAnimalInfo').textContent = `${name} - ${breed}`;

  // Update map
  if (map) {
    map.setView([lat, lng], 10);
    
    if (marker) map.removeLayer(marker);
    marker = L.marker([lat, lng]).addTo(map);
    marker.bindPopup(`<strong>${name}</strong><br>Breed: ${breed}<br>Lat: ${lat.toFixed(4)}<br>Lng: ${lng.toFixed(4)}`).openPopup();
  }
}

// Update display
function updateDisplay() {
  applySearch();
  renderTable();
  renderChart();
  updateMapLocation();
}

// Start the app when page loads
window.addEventListener('DOMContentLoaded', init);
