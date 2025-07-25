const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 3001;

// Middleware
app.use(cors());
app.use(express.json());

// Sample POST route
app.post('/api/feedback', (req, res) => {
  const { content } = req.body;
  console.log(`Received feedback: ${content} at ${new Date().toISOString()}`);

  // 暂时不保存数据库，只回传响应
  res.json({ message: 'Feedback received successfully' });
});

// Start server
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});

