import './App.css';
import Enter from "./components/Middle/Main/Enter/Enter";
import Index from "./components/Middle/Main/Index/Index";
import React, { useEffect, useState, useCallback } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Application from "./Application";
import axios from "axios";
import Users from "./components/Middle/Main/Users/Users";
import NotFound from "./components/Middle/Main/NotFound/NotFound";
import ProtectedRoute from "./components/ProtectedRoute";
import Register from "./components/Middle/Main/Register/Register";
import WritePost from "./components/Middle/Main/WritePost/WritePost";
import Post from "./components/Middle/Main/Post/Post";

function App() {
    const [login, setLogin] = useState(null);
    const [isLoadingAuth, setIsLoadingAuth] = useState(true);
    const [page, setPage] = useState('index');
    const [currentPostId, setCurrentPostId] = useState(null);
    const [posts, setPosts] = useState([]);
    const [isLoadingPosts, setIsLoadingPosts] = useState(true);

    useEffect(() => {
        const interceptor = axios.interceptors.response.use(
            response => response,
            error => {
                if (error.response?.status === 401) {
                    localStorage.removeItem('jwt');
                    localStorage.removeItem('login');
                    window.location.href = '/enter';
                }
                return Promise.reject(error);
            }
        );
        return () => axios.interceptors.response.eject(interceptor);
    }, []);

    const loadPosts = useCallback(async () => {
        try {
            setIsLoadingPosts(true);
            const response = await axios.get('/api/posts');
            setPosts(response.data);
        } catch (error) {
            console.error('Failed to load posts:', error);
        } finally {
            setIsLoadingPosts(false);
        }
    }, []);

    const createPost = useCallback(async (postData) => {
        try {
            const jwt = localStorage.getItem('jwt');
            await axios.post('/api/posts', postData, {
                headers: {
                    'Authorization': `Bearer ${jwt}`
                }
            });
            await loadPosts();
            return true;
        } catch (error) {
            console.error('Failed to create post:', error);
            return false;
        }
    }, [loadPosts]);

    useEffect(() => {
        const verifyAuth = async () => {
            try {
                const jwt = localStorage.getItem("jwt");
                if (jwt) {
                    const response = await axios.get("/api/jwt", {
                        params: { jwt }
                    });
                    localStorage.setItem("login", response.data.login);
                    setLogin(response.data.login);
                }
            } catch (error) {
                localStorage.removeItem("jwt");
                localStorage.removeItem("login");
                setLogin(null);
            } finally {
                setIsLoadingAuth(false);
            }
        };

        verifyAuth();
    }, []);

    useEffect(() => {
        loadPosts();
    }, [loadPosts]);

    if (isLoadingAuth) {
        return <div className="loading">Loading authentication...</div>;
    }

    return (
        <div className="App">
            <BrowserRouter>
                <Routes>
                    <Route
                        path="/"
                        element={
                            <Application
                                setLogin={setLogin}
                                login={login}
                                posts={posts}
                                isLoadingPosts={isLoadingPosts}
                                reloadPosts={loadPosts}
                                page={
                                    <Index
                                        login={login}
                                        setPage={setPage}
                                        setCurrentPostId={setCurrentPostId}
                                        posts={posts}
                                    />
                                }
                            />
                        }
                    />

                    <Route
                        path="/enter"
                        element={
                            <Application
                                setLogin={setLogin}
                                login={login}
                                posts={posts}
                                isLoadingPosts={isLoadingPosts}
                                reloadPosts={loadPosts}
                                page={<Enter setLogin={setLogin} />}
                            />
                        }
                    />

                    <Route
                        path="/register"
                        element={
                            <Application
                                setLogin={setLogin}
                                login={login}
                                posts={posts}
                                isLoadingPosts={isLoadingPosts}
                                reloadPosts={loadPosts}
                                page={
                                    <Register
                                        setLogin={setLogin}
                                        setPage={setPage}
                                    />
                                }
                            />
                        }
                    />

                    <Route
                        path="/write-post"
                        element={
                            <ProtectedRoute login={login} isLoadingAuth={isLoadingAuth}>
                                <Application
                                    setLogin={setLogin}
                                    login={login}
                                    posts={posts}
                                    isLoadingPosts={isLoadingPosts}
                                    reloadPosts={loadPosts}
                                    page={
                                        <WritePost
                                            setPage={setPage}
                                            createPost={createPost}
                                        />
                                    }
                                />
                            </ProtectedRoute>
                        }
                    />

                    <Route
                        path="/posts/:id"
                        element={
                            <Application
                                setLogin={setLogin}
                                login={login}
                                posts={posts}
                                isLoadingPosts={isLoadingPosts}
                                reloadPosts={loadPosts}
                                page={<Post />}
                            />
                        }
                    />

                    <Route
                        path="/users"
                        element={
                            <ProtectedRoute login={login} isLoadingAuth={isLoadingAuth}>
                                <Application
                                    setLogin={setLogin}
                                    login={login}
                                    posts={posts}
                                    isLoadingPosts={isLoadingPosts}
                                    reloadPosts={loadPosts}
                                    page={<Users login={login} />}
                                />
                            </ProtectedRoute>
                        }
                    />

                    <Route
                        path="*"
                        element={
                            <Application
                                setLogin={setLogin}
                                login={login}
                                posts={posts}
                                isLoadingPosts={isLoadingPosts}
                                reloadPosts={loadPosts}
                                page={<NotFound />}
                            />
                        }
                    />
                </Routes>
            </BrowserRouter>
        </div>
    );
}

export default App;