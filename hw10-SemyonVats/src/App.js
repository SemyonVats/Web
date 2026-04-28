import './App.css';
import Enter from "./components/Middle/Main/Enter/Enter";
import WritePost from "./components/Middle/Main/WritePost/WritePost";
import Index from "./components/Middle/Main/Index/Index";
import Register from "./components/Middle/Main/Register/Register";
import Users from "./components/Middle/Main/Users/Users";
import Post from "./components/Middle/Main/Post/Post";
import React, { useCallback, useState } from "react";
import Middle from "./components/Middle/Middle";
import Footer from "./components/Footer/Footer";
import Header from "./components/Header/Header";

function App({ users: initialUsers = [], postsData = [], comments: initialComments = [] }) {
    const [user, setUser] = useState(null);
    const [page, setPage] = useState('index');
    const [posts, setPosts] = useState(postsData);
    const [users, setUsers] = useState(initialUsers);
    const [comments, setComments] = useState(initialComments);
    const [currentPostId, setCurrentPostId] = useState(null);

    const registerUser = useCallback((userData) => {
        setUsers(prev => {
            const maxId = prev.length > 0 ? Math.max(...prev.map(u => u.id)) : 0;
            return [...prev, {
                id: maxId + 1,
                login: userData.login,
                name: userData.name
            }];
        });
    }, []);

    const createPost = useCallback((post) => {
        setPosts(prev => {
            const maxId = prev.length > 0 ? Math.max(...prev.map(p => p.id)) : 0;
            return [...prev, {
                ...post,
                id: maxId + 1,
                author: user?.login || 'abobus'
            }];
        });
    }, [user]);

    const addComment = useCallback((postId, content) => {
        if (!user) return;

        setComments(prev => {
            const maxId = prev.length > 0 ? Math.max(...prev.map(c => c.id)) : 0;
            return [...prev, {
                id: maxId + 1,
                postId,
                content,
                author: user.login
            }];
        });
    }, [user]);

    const getPage = useCallback((page) => {
        switch (page) {
            case 'index':
                return <Index
                    posts={posts}
                    setCurrentPostId={setCurrentPostId}
                    setPage={setPage}
                />;
            case 'users':
                return <Users users={users} />;
            case 'enter':
                return <Enter users={users} setUser={setUser} setPage={setPage} />;
            case 'writePost':
                return <WritePost createPost={createPost} setPage={setPage} user={user} />;
            case 'register':
                return <Register
                    users={users}
                    onRegister={registerUser}
                    setPage={setPage}
                />;
            case 'posts':
                return <Index
                    posts={posts}
                    setPage={setPage}
                    setCurrentPostId={setCurrentPostId}
                />;
            case 'post':
                const post = posts.find(p => p.id === currentPostId);
                const postComments = comments.filter(c => c.postId === currentPostId);
                return post ? (
                    <Post
                        post={post}
                        comments={postComments}
                        addComment={addComment}
                        user={user}
                    />
                ) : (
                    <div className="error">Post not found</div>
                );
            default:
                return <Index
                    posts={posts}
                    setCurrentPostId={setCurrentPostId}
                    setPage={setPage}
                />;
        }
    }, [posts, users, comments, currentPostId, createPost, registerUser, user, addComment]);

    return (
        <div className="App">
            <Header user={user} setUser={setUser} setPage={setPage} />{}
            <Middle
                posts={posts}
                page={getPage(page)}
                setPage={setPage}
                setCurrentPostId={setCurrentPostId}
            />
            <Footer userCount={users.length} postCount={posts.length} />
        </div>
    );
}

export default App;