import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const ProtectedRoute = ({ children, login, isLoadingAuth }) => {
    const navigate = useNavigate();

    useEffect(() => {
        if (!isLoadingAuth && !login) {
            console.log('Accessing protected route without authentication');
            localStorage.removeItem('jwt');
            localStorage.removeItem('login');
            window.location.href = '/enter';
        }
    }, [login, isLoadingAuth, navigate]);

    if (isLoadingAuth) {
        return <div className="loading">Verifying authentication...</div>;
    }

    if (!login) {
        return null;
    }

    return children;
};

export default ProtectedRoute;