/*
 * This file is generated by jOOQ.
 */
package org.example.generated.tables.records;


import java.time.LocalDateTime;

import org.example.generated.tables.YoutubeCache;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * The table <code>public.youtube_cache</code>.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class YoutubeCacheRecord extends UpdatableRecordImpl<YoutubeCacheRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.youtube_cache.query</code>.
     */
    public void setQuery(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.youtube_cache.query</code>.
     */
    public String getQuery() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.youtube_cache.results</code>.
     */
    public void setResults(JSONB value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.youtube_cache.results</code>.
     */
    public JSONB getResults() {
        return (JSONB) get(1);
    }

    /**
     * Setter for <code>public.youtube_cache.created_at</code>.
     */
    public void setCreatedAt(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.youtube_cache.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached YoutubeCacheRecord
     */
    public YoutubeCacheRecord() {
        super(YoutubeCache.YOUTUBE_CACHE);
    }

    /**
     * Create a detached, initialised YoutubeCacheRecord
     */
    public YoutubeCacheRecord(String query, JSONB results, LocalDateTime createdAt) {
        super(YoutubeCache.YOUTUBE_CACHE);

        setQuery(query);
        setResults(results);
        setCreatedAt(createdAt);
        resetChangedOnNotNull();
    }
}
