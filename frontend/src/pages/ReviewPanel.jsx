import { useEffect, useState } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import axios from 'axios'

export default function ReviewPanel() {
  const { paperId } = useParams()
  const [paper, setPaper] = useState(null)
  const [review, setReview] = useState({
    overallRating: 5,
    technicalQualityRating: 5,
    clarityRating: 5,
    originalityRating: 5,
    significanceRating: 5,
    comments: ''
  })
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    fetchPaper()
  }, [paperId])

  const fetchPaper = async () => {
    try {
      const response = await axios.get(`/api/papers/${paperId}`, {
        headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
      })
      setPaper(response.data)
    } catch (error) {
      console.error('Failed to fetch paper:', error)
      navigate('/dashboard')
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setReview(prev => ({
      ...prev,
      [name]: isNaN(value) ? value : parseInt(value)
    }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSubmitting(true)

    try {
      await axios.post(`/api/reviews/${paperId}`, review, {
        headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
      })
      navigate('/dashboard')
    } catch (error) {
      console.error('Failed to submit review:', error)
      alert('Failed to submit review')
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) return <div className="text-center mt-4">Loading...</div>
  if (!paper) return <div className="text-center mt-4">Paper not found</div>

  return (
    <div className="app">
      <nav className="navbar">
        <div className="navbar-brand">Research Journal Management</div>
        <ul className="nav-links">
          <li><Link to="/dashboard">Dashboard</Link></li>
        </ul>
      </nav>

      <div className="container">
        <Link to="/dashboard" className="btn btn-secondary mb-3">← Back</Link>

        <div className="card">
          <h1>Review Paper: {paper.title}</h1>
          <p><strong>Author:</strong> {paper.author?.firstName} {paper.author?.lastName}</p>
          <p><strong>Status:</strong> {paper.status}</p>

          <div className="mt-4" style={{ maxHeight: '400px', overflowY: 'auto', backgroundColor: '#f9fafb', padding: '1rem', borderRadius: '0.375rem' }}>
            <h3>Paper Abstract</h3>
            <p>{paper.abstractText}</p>
            <h3>Paper Content</h3>
            <div style={{ whiteSpace: 'pre-wrap', fontSize: '0.9rem' }}>
              {paper.content?.substring(0, 1000)}...
            </div>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="card mt-4">
          <h2>Submit Your Review</h2>

          <div className="form-group">
            <label>Overall Rating (1-10)</label>
            <input
              type="number"
              name="overallRating"
              value={review.overallRating}
              onChange={handleChange}
              min="1"
              max="10"
              required
            />
          </div>

          <div className="form-group">
            <label>Technical Quality (1-10)</label>
            <input
              type="number"
              name="technicalQualityRating"
              value={review.technicalQualityRating}
              onChange={handleChange}
              min="1"
              max="10"
              required
            />
          </div>

          <div className="form-group">
            <label>Clarity (1-10)</label>
            <input
              type="number"
              name="clarityRating"
              value={review.clarityRating}
              onChange={handleChange}
              min="1"
              max="10"
              required
            />
          </div>

          <div className="form-group">
            <label>Originality (1-10)</label>
            <input
              type="number"
              name="originalityRating"
              value={review.originalityRating}
              onChange={handleChange}
              min="1"
              max="10"
              required
            />
          </div>

          <div className="form-group">
            <label>Significance (1-10)</label>
            <input
              type="number"
              name="significanceRating"
              value={review.significanceRating}
              onChange={handleChange}
              min="1"
              max="10"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="comments">Comments and Feedback</label>
            <textarea
              id="comments"
              name="comments"
              value={review.comments}
              onChange={handleChange}
              required
              placeholder="Provide detailed feedback for the authors"
            />
          </div>

          <button type="submit" className="btn btn-success" disabled={submitting}>
            {submitting ? 'Submitting...' : 'Submit Review'}
          </button>
        </form>
      </div>
    </div>
  )
}
