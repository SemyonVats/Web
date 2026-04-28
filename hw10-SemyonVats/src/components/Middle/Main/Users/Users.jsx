import React from 'react';

const Users = ({ users = [] }) => {
    return (
        <div className="users-container">
            <h1>All users</h1>
            {users.length === 0 ? (
                <p>No registered users</p>
            ) : (
                <div className="datatable">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Login</th>
                            <th>Name</th>
                        </tr>
                        </thead>
                        <tbody>
                        {users.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.login}</td>
                                <td>{user.name}</td>
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