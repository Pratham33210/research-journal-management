import { useEffect, useState } from 'react'
import { useParams, useNavigate, Link } from 'react-router-dom'
import axios from 'axios'

export default function PaperDetails() {
  const { id } = useParams()
  const [paper, setPaper] = useState(null)
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  useEffect(() => {
    fetchPaper()
  }, [id])

  const fetchPaper = async () => {
    try {
      const response = await axios.get(`/api/papers/${id}`, {
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
          <h1>{paper.title}</h1>
          <p><strong>Author:</strong> {paper.author?.firstName} {paper.author?.lastName}</p>
          <p><strong>Status:</strong> <span className={`badge badge-${paper.status?.toLowerCase()}`}>{paper.status}</span></p>
          <p><strong>Submitted:</strong> {new Date(paper.submittedAt).toLocaleDateString()}</p>

          {paper.plagiarismChecked && (
            <p><strong>Plagiarism Score:</strong> {(paper.plagiarismScore * 100).toFixed(2)}%</p>
          )}

          <h3>Abstract</h3>
          <p>{paper.abstractText}</p>

          <h3>Content</h3>
          <div style={{ whiteSpace: 'pre-wrap', lineHeight: '1.6' }}>
            {paper.content}
          </div>

          {paper.reviews && paper.reviews.length > 0 && (
            <div className="mt-4">
              <h3>Reviews</h3>
              {paper.reviews.map(review => (
                <div key={review.id} className="card" style={{ marginTop: '1rem', backgroundColor: '#f9fafb' }}>
                  <p><strong>Reviewer:</strong> {review.reviewer?.firstName} {review.reviewer?.lastName}</p>
                  <p><strong>Overall Rating:</strong> {review.overallRating}/10</p>
                  <p><strong>Comments:</strong> {review.comments}</p>
                </div>
              ))}
            </div>
          )}

          {paper.revisions && paper.revisions.length > 0 && (
            <div className="mt-4">
              <h3>Revisions</h3>
              {paper.revisions.map(revision => (
                <div key={revision.id} className="card" style={{ marginTop: '1rem', backgroundColor: '#f9fafb' }}>
                  <p><strong>Revision {revision.revisionNumber}</strong> - {new Date(revision.submittedAt).toLocaleDateString()}</p>
                  <p><strong>Changes:</strong> {revision.changesSummary}</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
