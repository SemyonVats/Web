import React from 'react';
import './Index.css';

const Index = ({ posts = [], setPage, setCurrentPostId }) => {
    const sortedPosts = [...posts].sort((a, b) => b.id - a.id);

    const handlePostClick = (postId) => {
        setCurrentPostId(postId);
        setPage('post');
    };

    return (
        <div className="posts-container">
            {sortedPosts.length === 0 ? (
                <p>No posts available</p>
            ) : (
                sortedPosts.map(post => {
                    return (
                        <article key={post.id} className="post">
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
                                {((post.content || post.text || '').length > 200)
                                    ? (post.content || post.text || '').slice(0, 200) + '...'
                                    : (post.content || post.text || '')}
                            </div>
                        </article>
                    );
                })
            )}
        </div>
    );
};

export default Index;