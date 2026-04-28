import React, { useState } from 'react';
import './Post.css';

const PostContent = ({ post }) => (
    <article className="full-post">
        <h1>{post.title}</h1>
        <div className="post-meta">
            <span className="author">Author: {post.author} </span>
        </div>
        <div className="post-content">
            {post.content || post.text}
        </div>
    </article>
);

const Post = ({post, comments = [], addComment, user}) => {
    const [newComment, setNewComment] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!newComment.trim() || !user) return;

        addComment(post.id, newComment.trim());
        setNewComment('');
    };

    return (
        <div className="post-page">
            <PostContent post={post} />

            <div className="comments-section">
                <h2>Comments ({comments.length})</h2>

                {user ? (
                    <form onSubmit={handleSubmit} className="comment-form">
                        <textarea
                            value={newComment}
                            onChange={(e) => setNewComment(e.target.value)}
                            placeholder="Write your comment..."
                            rows="3"
                        />
                        <button type="submit">Add Comment</button>
                    </form>
                ) : (
                    <p className="login-prompt">
                        Please login to comment
                    </p>
                )}

                <div className="comments-list">
                    {comments.length === 0 ? (
                        <p className="no-comments">No comments yet. Be the first!</p>
                    ) : (
                        comments.map(comment => (
                            <div key={comment.id} className="comment-item">
                                <div className="comment-header">
                                    <span className="comment-author">{comment.author}</span>
                                </div>
                                <div className="comment-content">
                                    {comment.content}
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>
        </div>
    );
};

export default Post;