const express = require('express');
const cors = require('cors');
const fs = require('fs');
const csv = require('csv-parser');
const path = require('path');

const app = express();
const PORT = 3000;

app.use(cors());
app.use(express.json());

app.use(express.static(path.join(__dirname), {
  setHeaders: (res, filepath) => {
    if (filepath.endsWith('.css')) {
      res.setHeader('Content-Type', 'text/css; charset=utf-8');
    } else if (filepath.endsWith('.js')) {
      res.setHeader('Content-Type', 'application/javascript; charset=utf-8');
    }
  }
}));

// Store data in memory
let animalData = [];

// Load CSV data
function loadCSVData() {
  const csvPath = path.join(__dirname, 'aac_shelter_outcomes.csv');
  animalData = [];
  
  return new Promise((resolve, reject) => {
    fs.createReadStream(csvPath)
      .pipe(csv())
      .on('data', (row) => {
        // Convert age and location columns to numbers
        row.age_upon_outcome_in_weeks = parseInt(row.age_upon_outcome_in_weeks) || 0;
        row.location_lat = parseFloat(row.location_lat) || 30.75;
        row.location_long = parseFloat(row.location_long) || -97.48;
        animalData.push(row);
      })
      .on('end', () => {
        console.log(`Loaded ${animalData.length} records from CSV`);
        resolve();
      })
      .on('error', (err) => {
        console.error('Error loading CSV:', err);
        reject(err);
      });
  });
}

app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'dashboard.html'));
});

// API endpoint to get filtered animals
app.get('/api/animals', (req, res) => {
  const filterType = req.query.filter || 'All';
  let filtered = animalData;

  if (filterType === 'Water') {
    filtered = animalData.filter(animal => 
      animal.sex_upon_outcome === 'Intact Female' &&
      ['Labrador Retriever Mix', 'Chesa Bay Retr Mix', 'Newfoundland Mix',
       'Newfoundland/Labrador Retriever', 'Newfoundland/Australian Cattle Dog',
       'Newfoundland/Great Pyrenees'].includes(animal.breed) &&
      animal.age_upon_outcome_in_weeks >= 26 &&
      animal.age_upon_outcome_in_weeks <= 156
    );
  } else if (filterType === 'Mountain') {
    filtered = animalData.filter(animal =>
      animal.sex_upon_outcome === 'Intact Male' &&
      ['German Shepherd', 'Alaskan Malamute', 'Old English Sheepdog',
       'Rottweiler', 'Siberian Husky'].includes(animal.breed) &&
      animal.age_upon_outcome_in_weeks >= 26 &&
      animal.age_upon_outcome_in_weeks <= 156
    );
  } else if (filterType === 'Disaster') {
    filtered = animalData.filter(animal =>
      animal.sex_upon_outcome === 'Intact Male' &&
      ['Doberman Pinscher', 'German Shepherd', 'Golden Retriever',
       'Bloodhound', 'Rottweiler'].includes(animal.breed) &&
      animal.age_upon_outcome_in_weeks >= 20 &&
      animal.age_upon_outcome_in_weeks <= 300
    );
  }

  res.json(filtered);
});

// Start server
loadCSVData()
  .then(() => {
    app.listen(PORT, () => {
      console.log(`AAC Shelter Dashboard Server`);
      console.log(`Running on http://localhost:${PORT}`);
      console.log(`${animalData.length} records loaded\n`);
    });
  })
  .catch(err => {
    console.error('Failed to start server:', err);
    process.exit(1);
  });
