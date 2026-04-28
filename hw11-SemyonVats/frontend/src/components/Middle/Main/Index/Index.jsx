import React from 'react';
import './Index.css';
import { useNavigate } from 'react-router-dom';

const Index = ({ login, setPage, setCurrentPostId, posts = [] }) => {
    const navigate = useNavigate();

    const sortedPosts = [...posts].sort((a, b) =>
        new Date(b.creationTime) - new Date(a.creationTime)
    );

    const handlePostClick = (postId) => {
        navigate(`/posts/${postId}`);
    };

    return (
        <div className="posts-container">
            {sortedPosts.length === 0 ? (
                <p>No posts available</p>
            ) : (
                sortedPosts.map(post => (
                    <article key={post.id} className="post-block">
                        <h2
                            onClick={() => handlePostClick(post.id)}
                            className="clickable-title"
                        >
                            {post.title}
                        </h2>
                        <div className="post-meta">
                            <span className="author">Author: {post.author || 'anonymous'}</span>
                        </div>
                        <div className="post-content">
                            {((post.text || '').length > 200)
                                ? (post.text || '').slice(0, 200) + '...'
                                : (post.text || '')}
                        </div>
                        <div className="view-all">
                            <button
                                onClick={() => handlePostClick(post.id)}
                                className="view-all-btn"
                            >
                                View All
                            </button>
                        </div>
                    </article>
                ))
            )}
        </div>
    );
};

export default Index;