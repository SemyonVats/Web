import React from 'react';

const Section = ({ post, setPage, setCurrentPostId }) => {
    const handleViewClick = (e) => {
        e.preventDefault();
        setCurrentPostId(post.id);
        setPage('post');
    };

    return (
        <section>
            <div className="header">
                {post.title}
            </div>
            <div className="body">
                {((post.content || post.text || '').length > 200)
                    ? (post.content || post.text || '').slice(0, 200) + '...'
                    : (post.content || post.text || '')}
            </div>
            <div className="footer">
                <a href="#" onClick={handleViewClick}>View all</a>
            </div>
        </section>
    );
};

export default Section;