import React from 'react'
import { Navigate, useNavigate } from 'react-router-dom';
import { useAppContext } from '../context/appContext'

const ProtectedRoute = ({ children }) => {

    const { token } = useAppContext();

    if (!token) {
        return <Navigate to='/landing' />
    }
    return (
        children
    )
}

export default ProtectedRoute