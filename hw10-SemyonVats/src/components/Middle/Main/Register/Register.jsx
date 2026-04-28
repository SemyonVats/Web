import React, { useRef, useState, useCallback } from 'react';

const Register = ({ users, onRegister, setPage }) => {
    const loginInputRef = useRef(null);
    const nameInputRef = useRef(null);
    const [error, setError] = useState(null);

    const handleSubmit = useCallback((e) => {
        e.preventDefault();

        const login = loginInputRef.current.value;
        const name = nameInputRef.current.value;
        let errorMsg = '';

        const loginRegex = /^[a-z]{3,16}$/;
        if (!loginRegex.test(login)) {
            errorMsg = 'Login must be 3-16 lowercase Latin letters';
        } else if (users.some(u => u.login === login)) {
            errorMsg = 'This login is already taken';
        } else if (name.length < 1 || name.length > 32) {
            errorMsg = 'Name must be 1-32 characters';
        }

        if (errorMsg) {
            setError(errorMsg);
            return;
        }

        onRegister({ login, name });
        setPage('enter');
    }, [users, onRegister, setPage]);

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
                                onChange={() => setError(null)}
                            />
                        </div>
                    </div>
                    <div className="field">
                        <div className="name">
                            <label htmlFor="name">Name</label>
                        </div>
                        <div className="value">
                            <input
                                name="name"
                                ref={nameInputRef}
                                onChange={() => setError(null)}
                            />
                        </div>
                    </div>
                    {error && <div className="error">{error}</div>}
                    <div className="button-field">
                        <input type="submit" value="Register" />
                    </div>
                </form>
            </div>
        </div>
    );
};

export default Register;