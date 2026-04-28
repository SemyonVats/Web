import React from 'react';
import { useNavigate } from "react-router-dom";

const EnterOrRegister = ({ login, setLogin }) => {
    const navigate = useNavigate();

    const handleEnterClick = (e) => {
        e.preventDefault();
        navigate('/enter');
    };

    const handleRegisterClick = (e) => {
        e.preventDefault();
        navigate('/register');
    };

    const handleLogoutClick = (e) => {
        e.preventDefault();
        setLogin(null);
        localStorage.removeItem("jwt");
        navigate('/');
    };

    return (
        <div className="enter-or-register-box">
            {login
                ?
                <>
                    <span className="current-user">{login}</span>
                    <a href="#" onClick={handleLogoutClick}>
                        Logout
                    </a>
                </>
                :
                <>
                    <a href="#" onClick={handleEnterClick}>Enter</a>
                    <a href="#" onClick={handleRegisterClick}>Register</a>
                </>
            }
        </div>
    );
};

export default EnterOrRegister;