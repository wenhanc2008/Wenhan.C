import React, { useState } from "react";

const Home = () => {
  const [content, setContent] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/feedback`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ content }),
    });

    const data = await response.json();
    console.log("Server response:", data);
    alert("Feedback submitted!");
    setContent(""); // 清空表单
  };

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Anonymous Feedback Form</h1>
      <form onSubmit={handleSubmit} className="flex flex-col space-y-4">
        <textarea
          value={content}
          onChange={(e) => setContent(e.target.value)}
          placeholder="Type your feedback here..."
          className="p-2 border border-gray-300 rounded"
        />
        <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
          Submit
        </button>
      </form>
    </div>
  );
};

export default Home;

