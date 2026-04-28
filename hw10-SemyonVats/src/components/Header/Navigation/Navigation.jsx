import React from 'react';

const Navigation = ({ user, setPage }) => {
    return (
        <nav>
            <ul>
                <li>
                    <a href="#" onClick={(event) => {
                        setPage('index');
                        event.preventDefault();
                    }}>Home</a>
                </li>
                <li>
                    <a href="#" onClick={(event) => {
                        setPage('users');
                        event.preventDefault();
                    }}>Users</a>
                </li>
                {user ? (
                    <li>
                        <a href="#" onClick={(event) => {
                            setPage('writePost');
                            event.preventDefault();
                        }}>
                            Write Post
                        </a>
                    </li>
                ) : null}
                <li>
                    <a href="#" onClick={(event) => {
                        setPage('posts');
                        event.preventDefault();
                    }}>Posts</a>
                </li>
            </ul>
        </nav>
    );
};

export default Navigation;