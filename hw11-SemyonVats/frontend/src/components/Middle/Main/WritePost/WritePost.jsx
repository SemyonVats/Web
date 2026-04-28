import React, { useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

const WritePost = ({ setPage, createPost }) => {
    const titleInputRef = useRef(null);
    const textInputRef = useRef(null);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (event) => {
        event.preventDefault();
        const title = titleInputRef.current.value.trim();
        const text = textInputRef.current.value.trim();

        if (title.length === 0 || text.length === 0) {
            setError('Title or text can not be empty');
            return;
        }

        try {
            setLoading(true);
            setError('');

            const success = await createPost({ title, text });
            if (success) {
                navigate("/");
            } else {
                setError('Failed to create post. Please try again.');
            }
        } catch (err) {
            setError('Failed to create post. Please try again.');
            console.error('Post creation error:', err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="form">
            <div className="header">Write Post</div>
            <div className="body">
                <form method="post" action="" onSubmit={handleSubmit}>
                    <input type="hidden" name="action" value="writePost"/>
                    <div className="field">
                        <div className="name">
                            <label htmlFor="title">Title</label>
                        </div>
                        <div className="value">
                            <input
                                autoFocus
                                id="title"
                                name="title"
                                ref={titleInputRef}
                                onChange={() => setError(null)}
                                disabled={loading}
                            />
                        </div>
                    </div>
                    <div className="field">
                        <div className="name">
                            <label htmlFor="text">Text</label>
                        </div>
                        <div className="value">
                            <textarea
                                id="text"
                                name="text"
                                ref={textInputRef}
                                onChange={() => setError(null)}
                                disabled={loading}
                            />
                        </div>
                    </div>
                    <div className="button-field">
                        <input
                            type="submit"
                            value={loading ? 'Creating...' : 'Write'}
                            disabled={loading}
                        />
                    </div>
                    {error && <div className="error">{error}</div>}
                </form>
            </div>
        </div>
    );
};

export default WritePost;