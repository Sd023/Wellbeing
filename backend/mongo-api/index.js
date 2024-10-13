const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const app = express();
const port = 3000;


app.use(express.json());
app.use(cors({
  origin: 'http://localhost:3001'
}));

mongoose.connect('mongodb://localhost:27017/mydb')
.then(() => console.log('Connected to MongoDB'))
.catch(err => console.error('Could not connect to MongoDB...', err));


const DBSchema = new mongoose.Schema({
    id: { type: Number, required: true },
    moduleCode: { type: String, required: true },
    actual: { type: Number, required: true },
    target: { type: Number, required: true },
    date : {type: String, required: true}
});


const DataBO = mongoose.model('Data', DBSchema);

app.post('/upload', async (req, res) => {
  const arrayObjects = req.body;
  console.log(req.body);

  try {
    await DataBO.insertMany(arrayObjects);
    res.status(201).send('Data inserted successfully');
  } catch (error) {
    console.log(error);
    res.status(500).send('Error inserting data');
  }
});

app.get('/download', async (req, res) => {
  try {
    const data = await DataBO.find();
    res.status(200).json(data);
  } catch (error) {
    res.status(500).send('Error fetching data');
  }
});

app.get('/api/totalActualTarget', async (req, res) => {
  try {
    const data = await DataBO.aggregate([
      {
        $group: {
          _id: "$moduleCode",
          totalActual: { $sum: "$actual" },
          totalTarget: { $sum: "$target" }
        }
      }
    ]);
    res.status(200).json(data);
  } catch (error) {
    res.status(500).send('Error fetching total actual and target');
  }
});


// app.get('/api/countModulesPerRange', async (req, res) => {
//   try {
//     const data = await DataBO.aggregate([
//       {
//         $bucket: {
//           groupBy: "$actual",
//           boundaries: [0, 50, 100, 150, 200],
//           default: "Other",
//           output: {
//             count: { $sum: 1 }
//           }
//         }
//       }
//     ]);
//     res.status(200).json(data);
//   } catch (error) {
//     res.status(500).send('Error fetching module count per actual value range');
//   }
// });


app.get('/api/dailyActualValues', async (req, res) => {
  try {
    const data = await DataBO.aggregate([
      {
        $group: {
          _id: "$date",
          totalActual: { $sum: "$actual" },
          totalTarget : { $sum: "$target"}
        }
      },
      {
        $sort: { _id: 1 }
      }
    ]);
    res.status(200).json(data);
  } catch (error) {
    res.status(500).send('Error fetching daily actual values');
  }
});


// app.get('/api/comparisonActualTarget', async (req, res) => {
//   try {
//     const data = await DataBO.aggregate([
//       {
//         $group: {
//           _id: "$moduleCode",
//           totalActual: { $sum: "$actual" },
//           totalTarget: { $sum: "$target" }
//         }
//       }
//     ]);
//     res.status(200).json(data);
//   } catch (error) {
//     res.status(500).send('Error fetching comparison of actual vs target');
//   }
// });




app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
