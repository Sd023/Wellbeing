import React, { useEffect, useState } from 'react';
import { Bar } from 'react-chartjs-2';
import 'chart.js/auto';

const App = () => {
  const [totalActualTargetData, setTotalActualTargetData] = useState([]);
  const [dailyActualValuesData, setDailyActualValuesData] = useState([]);
  


   useEffect(() => {
    fetch('http://localhost:3000/api/totalActualTarget')
      .then(response => response.json())
      .then(data => setTotalActualTargetData(data))
      .catch(error => console.log(error));

    fetch('http://localhost:3000/api/dailyActualValues')
      .then(response => response.json())
      .then(data => setDailyActualValuesData(data))
      .catch(error => console.log(error));
  }, []);

 
  const prepareChartData = (data, label1, label2) => {
    const labels = data.map(item => item._id);
    const actualValues = data.map(item => item.totalActual);
    const targetValues = data.map(item => item.totalTarget);
    
    return {
      labels,
      datasets: [
        {
          label: label1,
          data: actualValues,
          backgroundColor: '#4BC0C0',
        },
        {
          label: label2,
          data: targetValues,
          backgroundColor: '#9966FF',
        }
      ]
    };
  };

  return (
    <div className="chart-container">
      <div className="chart">
        <h2>Total Actual vs Target</h2>
        {totalActualTargetData.length > 0 && (
          <Bar data={prepareChartData(totalActualTargetData, 'Actual', 'Target')} />
        )}
      </div>

      <div className="chart">
        <h2>Daily Actual Values</h2>
        {dailyActualValuesData.length > 0 && (
          <Bar data={prepareChartData(dailyActualValuesData, 'Daily Actual', '')} />
        )}
      </div>
    </div>
  );
};

export default App;
