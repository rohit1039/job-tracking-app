import React from 'react'
import { motion } from 'framer-motion'

export const AnimateForm = ({ children }) => {
    return (
        <motion.div initial={{ opacity: 0, y: 60 }} animate={{ opacity: 1, y: 0 }} exit={{ y: -60 }} transition={{ duration: 1 }}>
            {children}
        </motion.div>
    )
}