import React, {useMemo} from 'react';
import Aside from "./Aside/Aside";

const Middle = ({posts, page, setPage, setCurrentPostId}) => {

    const aside = useMemo(() => {
        return (
            <Aside
                posts={posts}
                setPage={setPage}
                setCurrentPostId={setCurrentPostId}
            />
        );
    }, [posts, setPage, setCurrentPostId]);

    return (
        <div className="middle">
            {aside}
            <main>
                {React.cloneElement(page, {
                    setPage,
                    setCurrentPostId
                })}
            </main>
        </div>
    );
};

export default Middle;