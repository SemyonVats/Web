import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';

const Post = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [post, setPost] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchPost = async () => {
            try {
                setLoading(true);
                const response = await axios.get(`/api/posts/${id}`);
                setPost(response.data);
            } catch (err) {
                navigate("/უცნაურიბმული")
                setError('Failed to load post');
                console.error('Error fetching post:', err);
            } finally {
                setLoading(false);
            }
        };

        fetchPost();
    }, [id]);

    const handleBackToHome = (e) => {
        e.preventDefault();
        navigate('/');
    };

    if (loading) {
        return <div className="loading">Loading post...</div>;
    }

    if (error) {
        return <div className="error">{error}</div>;
    }

    if (!post) {
        return <div className="not-found">Post not found</div>;
    }

    return (
        <div className="post">
            <h1 className="post-title">{post.title}</h1>
            <div className="post-meta">
                <span className="post-author">Author: {post.author}</span>
                <span className="post-date">
                    Created: {new Date(post.creationTime).toLocaleString()}
                </span>
            </div>
            <div className="post-content">
                {post.text}
            </div>
            <div className="back-link">
                <a href="/" onClick={handleBackToHome}>← Back to all posts</a>
            </div>
        </div>
    );
};

export default Post;