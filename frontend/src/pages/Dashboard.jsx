import { useEffect, useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import axios from 'axios'
import apiClient from '../api/apiClient'

export default function Dashboard() {
  const [papers, setPapers] = useState([])
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  useEffect(() => {
    const userData = localStorage.getItem('user')
    const token = localStorage.getItem('authToken')

    if (!token) {
      navigate('/login')
      return
    }

    setUser(JSON.parse(userData))
    fetchPapers()
  }, [navigate])

  const fetchPapers = async () => {
    try {
      const response = await apiClient.get('/papers')
      setPapers(response.data)
    } catch (error) {
      console.error('Failed to fetch papers:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleLogout = () => {
    localStorage.removeItem('authToken')
    localStorage.removeItem('user')
    navigate('/login')
  }

  if (loading) return <div className="text-center mt-4">Loading...</div>

  return (
    <div className="app">
      <nav className="navbar">
        <div className="navbar-brand">Research Journal Management</div>
        <ul className="nav-links">
          <li><Link to="/dashboard">Dashboard</Link></li>
          <li><Link to="/submit-paper">Submit Paper</Link></li>
          <li><button onClick={handleLogout} className="btn btn-secondary">Logout</button></li>
        </ul>
      </nav>

      <div className="container">
        <h1>Dashboard</h1>
        <p>Welcome, {user?.firstName} {user?.lastName}</p>

        <div className="mt-4">
          <h2>Your Papers</h2>
          {papers.length === 0 ? (
            <p className="text-muted">No papers submitted yet. <Link to="/submit-paper">Submit your first paper</Link></p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Title</th>
                  <th>Status</th>
                  <th>Submitted Date</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {papers.map(paper => (
                  <tr key={paper.id}>
                    <td>{paper.title}</td>
                    <td><span className={`badge badge-${paper.status.toLowerCase()}`}>{paper.status}</span></td>
                    <td>{new Date(paper.submittedAt).toLocaleDateString()}</td>
                    <td><Link to={`/paper/${paper.id}`} className="btn btn-primary">View</Link></td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  )
}
