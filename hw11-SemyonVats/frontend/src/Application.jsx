import './App.css';
import React, { useEffect, useState } from "react";
import Middle from "./components/Middle/Middle";
import Footer from "./components/Footer/Footer";
import Header from "./components/Header/Header";
import axios from "axios";

function Application({ page, login, setLogin }) {
    const [posts, setPosts] = useState(null);

    useEffect(() => {
        const loadPosts = async () => {
            try {
                const response = await axios.get("/api/posts");
                setPosts(response.data);
            } catch (error) {
                if (error.response?.status === 401) {
                    localStorage.removeItem('jwt');
                    localStorage.removeItem('login');
                    window.location.href = '/enter';
                    return;
                }
                console.error('Failed to load posts:', error);
            }
        };

        loadPosts();
    }, []);

    return (
        <div>
            <Header setLogin={setLogin} login={login} />
            <Middle
                posts={posts}
                page={page}
            />
            <Footer />
        </div>
    );
}

export default Application;