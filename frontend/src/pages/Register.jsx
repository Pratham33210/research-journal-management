import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import apiClient from '../api/apiClient'
import '../styles/Auth.css'

export default function Register() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    confirmPassword: '',
    firstName: '',
    lastName: '',
    affiliation: '',
    role: 'AUTHOR'
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const navigate = useNavigate()

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({ ...prev, [name]: value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')

    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match')
      return
    }

    if (formData.password.length < 6) {
      setError('Password must be at least 6 characters')
      return
    }

    setLoading(true)

    // AUTO-GENERATE USERNAME FROM EMAIL (this was the missing piece!)
    const username = formData.email.split('@')[0]
      .toLowerCase()
      .replace(/[^a-z0-9]/g, '')  // removes dots, numbers-only allowed

    try {
      const response = await apiClient.post('/auth/register', {
        username,                    // THIS WAS MISSING BEFORE
        email: formData.email,
        password: formData.password,
        firstName: formData.firstName,
        lastName: formData.lastName,
        affiliation: formData.affiliation,
        role: formData.role
      })

      // Save token + user if your backend returns it
      if (response.data.token) {
        localStorage.setItem('authToken', response.data.token)
        localStorage.setItem('user', JSON.stringify(response.data.user))
      }

      alert(`Welcome ${formData.firstName}! Registration successful!`)
      navigate('/dashboard')

    } catch (err) {
      console.error('Registration error:', err)
      const msg = err.response?.data?.message || err.response?.data || 'Registration failed. Try again.'
      setError(msg)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card register-card">
        <h1>Research Journal Management</h1>
        <h2>Register</h2>
        
        {error && <div className="alert alert-error">{error}</div>}
        
        <form onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label>First Name</label>
              <input
                type="text"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                required
                placeholder="Pratham"
              />
            </div>
            <div className="form-group">
              <label>Last Name</label>
              <input
                type="text"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                required
                placeholder="Heble"
              />
            </div>
          </div>

          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="123@gmail.com"
            />
          </div>

          <div className="form-group">
            <label>Affiliation</label>
            <input
              type="text"
              name="affiliation"
              value={formData.affiliation}
              onChange={handleChange}
              required
              placeholder="KL University"
            />
          </div>

          <div className="form-group">
            <label>Role</label>
            <select name="role" value={formData.role} onChange={handleChange}>
              <option value="AUTHOR">Author</option>
              <option value="REVIEWER">Reviewer</option>
            </select>
          </div>

          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Confirm Password</label>
            <input
              type="password"
              name="confirmPassword"
              value={formData.confirmPassword}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary" disabled={loading}>
            {loading ? 'Creating Account...' : 'Register'}
          </button>
        </form>

        <p className="text-center mt-4">
          Already have an account? <Link to="/login">Login here</Link>
        </p>
      </div>
    </div>
  )
}