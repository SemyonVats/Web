import React, { useRef, useState, useCallback } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Register = ({ setLogin }) => {
    const navigate = useNavigate();
    const loginInputRef = useRef(null);
    const passwordInputRef = useRef(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSubmit = useCallback(async (e) => {
        e.preventDefault();

        const login = loginInputRef.current.value.trim().toLowerCase();
        const password = passwordInputRef.current.value.trim();
        setError(null);

        if (!/^[a-z0-9]{3,16}$/.test(login)) {
            setError('Login must be 3-16 lowercase Latin letters or digits');
            return;
        }

        if (password.length < 8 || password.length > 32) {
            setError('Password must be 8-32 characters long');
            return;
        }

        try {
            setLoading(true);

            await axios.post('/api/users', {
                login,
                password
            });

            const jwtResponse = await axios.post('/api/jwt', {
                login,
                password
            });

            const jwt = jwtResponse.data;

            localStorage.setItem('jwt', jwt);
            localStorage.setItem('login', login);
            setLogin(login);

            navigate('/');

        } catch (err) {
            console.error('Registration error:', err);
            if (err.response) {
                if (err.response.status === 409) {
                    setError('This login is already taken');
                } else if (err.response.data && typeof err.response.data === 'string') {
                    setError(err.response.data);
                } else if (err.response.data && err.response.data.message) {
                    setError(err.response.data.message);
                } else {
                    setError('Registration failed. Please try again.');
                }
            } else if (err.request) {
                setError('Network error. Please check your connection.');
            } else {
                setError('An error occurred. Please try again.');
            }
        } finally {
            setLoading(false);
        }
    }, [setLogin, navigate]);

    return (
        <div className="register form-box">
            <div className="header">Register</div>
            <div className="body">
                <form onSubmit={handleSubmit}>
                    <div className="field">
                        <div className="name">
                            <label htmlFor="login">Login</label>
                        </div>
                        <div className="value">
                            <input
                                autoFocus
                                name="login"
                                ref={loginInputRef}
                                type="text"
                                onChange={() => setError(null)}
                                disabled={loading}
                            />
                        </div>
                    </div>
                    <div className="field">
                        <div className="name">
                            <label htmlFor="password">Password</label>
                        </div>
                        <div className="value">
                            <input
                                name="password"
                                ref={passwordInputRef}
                                type="password"
                                onChange={() => setError(null)}
                                disabled={loading}
                            />
                        </div>
                    </div>
                    {error && <div className="error">{error}</div>}
                    <div className="button-field">
                        <button
                            type="submit"
                            className="btn"
                            disabled={loading}
                        >
                            {loading ? 'Registering...' : 'Register'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Register;