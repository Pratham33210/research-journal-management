import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Dashboard from './pages/Dashboard'
import SubmitPaper from './pages/SubmitPaper'
import PaperDetails from './pages/PaperDetails'
import ReviewPanel from './pages/ReviewPanel'
import './App.css'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/submit-paper" element={<SubmitPaper />} />
        <Route path="/paper/:id" element={<PaperDetails />} />
        <Route path="/review/:paperId" element={<ReviewPanel />} />
        <Route path="/" element={<Login />} />
      </Routes>
    </Router>
  )
}

export default App
