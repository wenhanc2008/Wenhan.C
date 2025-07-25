import React, { useState } from 'react';

const Feedback = () => {
  // 状态：评分和反馈文本
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');

  // 提交反馈（先用 alert 测试）
  const handleSubmit = (e) => {
    e.preventDefault();
    alert(`Submitted rating: ${rating}\nComment: ${comment}`);
    // 后续这里会调用后端接口
    setRating(5);
    setComment('');
  };

  return (
    <div style={{ padding: '2rem' }}>
      <h2>Submit Your Anonymous Feedback</h2>
      <form onSubmit={handleSubmit}>
        <label>
          Course Rating (1-5):&nbsp;
          <select value={rating} onChange={e => setRating(Number(e.target.value))}>
            {[1,2,3,4,5].map(num => (
              <option key={num} value={num}>{num}</option>
            ))}
          </select>
        </label>
        <br /><br />
        <label>
          Your Feedback:
          <br />
          <textarea
            rows="5"
            cols="50"
            value={comment}
            onChange={e => setComment(e.target.value)}
            placeholder="Write your feedback here..."
            required
          />
        </label>
        <br /><br />
        <button type="submit">Submit Feedback</button>
      </form>
    </div>
  );
};

export default Feedback;
