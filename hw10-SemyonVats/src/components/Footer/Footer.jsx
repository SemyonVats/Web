import React from 'react';

const Footer = ({ userCount, postCount }) => {
    return (
        <footer>
            <a href="#">Codehorses</a> 2099 by Mike Mirzayanov.
            Users: {userCount}, Posts: {postCount}
        </footer>
    );
};

export default Footer;