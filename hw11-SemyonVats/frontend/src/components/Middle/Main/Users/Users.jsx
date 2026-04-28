import React, { useState, useEffect } from 'react';
import axios from 'axios';

const Users = ({ login }) => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const response = await axios.get('/api/users');
                setUsers(response.data);
            } catch (error) {
                if (error.response?.status !== 401) {
                    console.error('Failed to fetch users:', error);
                }
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);

    if (loading) {
        return <div className="loading">Loading users...</div>;
    }

    return (
        <div className="users-container">
            <h1>All Users</h1>
            {users.length === 0 ? (
                <p>No registered users</p>
            ) : (
                <div className="datatable">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Login</th>
                            <th>Creation Time</th>
                        </tr>
                        </thead>
                        <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.login}</td>
                                <td>{new Date(user.creationTime).toLocaleString()}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
};

export default Users;