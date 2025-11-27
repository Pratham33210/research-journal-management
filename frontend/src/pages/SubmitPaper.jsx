import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import axios from 'axios'

export default function SubmitPaper() {
  const [formData, setFormData] = useState({
    title: '',
    abstractText: '',
    content: ''
  })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    setLoading(true)

    try {
          const response = await axios.post('/api/papers', formData, {
            headers: { Authorization: `Bearer ${localStorage.getItem('authToken')}` }
      })
      setSuccess('Paper submitted successfully!')
      setTimeout(() => navigate('/dashboard'), 2000)
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit paper')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="app">
      <nav className="navbar">
        <div className="navbar-brand">Research Journal Management</div>
        <ul className="nav-links">
          <li><Link to="/dashboard">Dashboard</Link></li>
          <li><Link to="/submit-paper">Submit Paper</Link></li>
        </ul>
      </nav>

      <div className="container">
        <h1>Submit Paper</h1>

        {error && <div className="alert alert-error">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}

        <form onSubmit={handleSubmit} className="card">
          <div className="form-group">
            <label htmlFor="title">Paper Title</label>
            <input
              id="title"
              type="text"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="Enter paper title"
            />
          </div>

          <div className="form-group">
            <label htmlFor="abstractText">Abstract</label>
            <textarea
              id="abstractText"
              name="abstractText"
              value={formData.abstractText}
              onChange={handleChange}
              required
              placeholder="Enter paper abstract"
            />
          </div>

          <div className="form-group">
            <label htmlFor="content">Paper Content</label>
            <textarea
              id="content"
              name="content"
              value={formData.content}
              onChange={handleChange}
              required
              placeholder="Enter full paper content"
              style={{ minHeight: '400px' }}
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Submitting...' : 'Submit Paper'}
          </button>
          <Link to="/dashboard" className="btn btn-secondary" style={{ marginLeft: '1rem' }}>
            Cancel
          </Link>
        </form>
      </div>
    </div>
  )
}
