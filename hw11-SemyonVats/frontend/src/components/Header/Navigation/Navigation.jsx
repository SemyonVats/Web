import React from 'react';
import { useNavigate } from "react-router-dom";

const Navigation = ({ login }) => {
    const router = useNavigate();

    return (
        <nav>
            <ul>
                <li>
                    <a href="" onClick={(event) => {
                        event.preventDefault();
                        router("/");
                    }}>Home</a>
                </li>
                <li>
                    <a href="" onClick={(event) => {
                        event.preventDefault();
                        router("/users");
                    }}>Users</a>
                </li>
                {login && (
                    <li>
                        <a href="" onClick={(event) => {
                            event.preventDefault();
                            router("/write-post");
                        }}>
                            Write Post
                        </a>
                    </li>
                )}
            </ul>
        </nav>
    );
};

export default Navigation;