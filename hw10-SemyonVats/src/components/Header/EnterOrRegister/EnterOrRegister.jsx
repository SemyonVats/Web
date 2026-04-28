import React from 'react';

const EnterOrRegister = ({ user, setUser, setPage }) => {
    return (
        <div className="enter-or-register-box">
            {user
                ? (
                    <>
                        {user.name} ({user.login})
                        <a href="#" onClick={(e) => {
                            setUser(null);
                            e.preventDefault();
                        }}>Logout</a>
                    </>
                )
                : (
                    <>
                        <a href="#" onClick={(e) => {
                            setPage('enter');
                            e.preventDefault();
                        }}>Enter</a>
                        <a href="#" onClick={(e) => {
                            setPage('register');
                            e.preventDefault();
                        }}>Register</a>
                    </>
                )
            }
        </div>
    );
};

export default EnterOrRegister;