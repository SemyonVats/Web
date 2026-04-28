import React from 'react';
import { useNavigate } from 'react-router-dom';

const Section = ({ post }) => {
    const navigate = useNavigate();

    const handleViewAllClick = (e) => {
        e.preventDefault();
        navigate(`/posts/${post.id}`);
    };

    return (
        <section>
            <div className="header">
                {post.title}
            </div>
            <div className="body">
                {post.text?.length > 100
                    ? post.text.slice(0, 100) + '...'
                    : post.text}
            </div>
            <div className="footer">
                <a href="#" onClick={handleViewAllClick}>
                    View all
                </a>
            </div>
        </section>
    );
};

export default Section;